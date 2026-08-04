package com.sumitra.resume.controller;

import com.sumitra.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam MultipartFile file,
            Authentication authentication) {

        String resumeId = resumeService.uploadResume(
                file,
                authentication.getName()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "Resume uploaded successfully",
                        "resumeId", resumeId
                )
        );
    }
}