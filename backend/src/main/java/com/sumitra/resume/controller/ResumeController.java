package com.sumitra.resume.controller;

import com.sumitra.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public String uploadResume(
            @RequestParam MultipartFile file,
            Authentication authentication) {

        return resumeService.uploadResume(
                file,
                authentication.getName()
        );
    }
}
