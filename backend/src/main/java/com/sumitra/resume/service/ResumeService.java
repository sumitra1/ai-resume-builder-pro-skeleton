package com.sumitra.resume.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    String uploadResume(MultipartFile file, String email);

    Resource downloadResume(String resumeId, String email);

    byte[] exportPdf(String title, String content);
}
