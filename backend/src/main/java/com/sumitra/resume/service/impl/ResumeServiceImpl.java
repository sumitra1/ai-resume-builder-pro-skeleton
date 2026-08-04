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
import com.sumitra.resume.util.TextChunker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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

            for (int i = 0; i < chunks.size(); i++) {

                ResumeChunk chunk = resumeEmbeddingService.createChunk(chunks.get(i));
                chunk.setResumeId(resumeId);
                chromaVectorStoreService.upsertResumeChunk(chunk);
            }

            return resumeId;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Resume upload failed", e);
        }
    }
}
