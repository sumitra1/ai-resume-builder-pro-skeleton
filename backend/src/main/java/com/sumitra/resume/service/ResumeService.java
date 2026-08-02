package com.sumitra.resume.service;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    String uploadResume(MultipartFile file, String email);

}
