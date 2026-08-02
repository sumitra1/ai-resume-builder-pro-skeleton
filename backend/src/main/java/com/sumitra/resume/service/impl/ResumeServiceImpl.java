package com.sumitra.resume.service.impl;

import com.sumitra.resume.entity.Resume;
import com.sumitra.resume.entity.User;
import com.sumitra.resume.repository.ResumeRepository;
import com.sumitra.resume.repository.UserRepository;
import com.sumitra.resume.service.ResumeService;
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

            System.out.println("Upload Directory : " + dir.getAbsolutePath());
            System.out.println("Destination File : " + destination.getAbsolutePath());
            System.out.println("Directory Exists : " + dir.exists());

            file.transferTo(destination);

            String resumeText = pdfExtractor.extractText(destination);

            List<String> chunks = textChunker.chunkText(resumeText);
            System.out.println("Total Chunks : " + chunks.size());
            for (int i = 0; i < chunks.size(); i++) {
                System.out.println("========== Chunk " + (i + 1) + " ==========");
                System.out.println(chunks.get(i));
            }

            Resume resume = new Resume();

            resume.setFileName(file.getOriginalFilename());
            resume.setFilePath(destination.getAbsolutePath());
            resume.setResumeText(resumeText);
            resume.setUploadedAt(LocalDateTime.now());
            resume.setUser(user);

            resumeRepository.save(resume);

            return "Resume uploaded successfully";

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Resume upload failed", e);
        }

    }
}
