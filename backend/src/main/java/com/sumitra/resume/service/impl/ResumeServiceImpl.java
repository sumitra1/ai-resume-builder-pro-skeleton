package com.sumitra.resume.service.impl;

import com.sumitra.resume.entity.Resume;
import com.sumitra.resume.entity.User;
import com.sumitra.resume.repository.ResumeRepository;
import com.sumitra.resume.repository.UserRepository;
import com.sumitra.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

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

            Resume resume = new Resume();

            resume.setFileName(file.getOriginalFilename());
            resume.setFilePath(destination.getAbsolutePath());
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
