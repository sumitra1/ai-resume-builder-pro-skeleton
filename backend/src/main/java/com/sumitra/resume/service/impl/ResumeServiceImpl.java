package com.sumitra.resume.service.impl;

import com.sumitra.resume.entity.Resume;
import com.sumitra.resume.entity.User;
import com.sumitra.resume.model.ResumeChunk;
import com.sumitra.resume.repository.ResumeRepository;
import com.sumitra.resume.repository.UserRepository;
import com.sumitra.resume.service.ResumeEmbeddingService;
import com.sumitra.resume.service.ResumeService;
import com.sumitra.resume.service.vectorstore.ChromaVectorStoreService;
import com.sumitra.resume.util.PdfExtractor;
import com.sumitra.resume.util.PdfGenerator;
import com.sumitra.resume.util.TextChunker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final PdfExtractor pdfExtractor;
    private final TextChunker textChunker;
    private final ResumeEmbeddingService resumeEmbeddingService;
    private final ChromaVectorStoreService chromaVectorStoreService;
    private final PdfGenerator pdfGenerator;

    @Override
    public String uploadResume(MultipartFile file, String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "." + extension;
            File destination = new File(dir, fileName);

            file.transferTo(destination);
            String resumeText = pdfExtractor.extractText(destination);

            Resume resume = new Resume();
            resume.setFileName(file.getOriginalFilename());
            resume.setFilePath(destination.getAbsolutePath());
            resume.setResumeText(resumeText);
            resume.setUploadedAt(LocalDateTime.now());
            resume.setUser(user);

            Resume savedResume = resumeRepository.save(resume);
            String resumeId = savedResume.getId().toString();

            List<String> chunks = textChunker.chunkText(resumeText);

            for (String chunkText : chunks) {
                ResumeChunk chunk = resumeEmbeddingService.createChunk(chunkText);
                chunk.setResumeId(resumeId);
                chromaVectorStoreService.upsertResumeChunk(chunk);
            }

            return resumeId;
        } catch (Exception e) {
            throw new RuntimeException("Resume upload failed", e);
        }
    }

    @Override
    public Resource downloadResume(String resumeId, String email) {
        Resume resume = findResumeForUser(resumeId, email);
        File file = new File(resume.getFilePath());
        if (!file.exists()) {
            throw new RuntimeException("Resume file not found on disk");
        }
        return new FileSystemResource(file);
    }

    @Override
    public byte[] exportPdf(String title, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content is required");
        }
        return pdfGenerator.createPdfFromText(title, content);
    }

    private Resume findResumeForUser(String resumeId, String email) {
        Resume resume = resumeRepository.findById(Long.parseLong(resumeId))
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not authorized to access this resume");
        }

        return resume;
    }
}
