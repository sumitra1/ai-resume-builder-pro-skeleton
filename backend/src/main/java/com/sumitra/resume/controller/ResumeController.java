package com.sumitra.resume.controller;

import com.sumitra.resume.dto.ExportPdfRequest;
import com.sumitra.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/download/{resumeId}")
    public ResponseEntity<Resource> downloadResume(
            @PathVariable String resumeId,
            Authentication authentication) {

        Resource resource = resumeService.downloadResume(resumeId, authentication.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestBody ExportPdfRequest request) {
        String title = request.getTitle() != null ? request.getTitle() : "Resume";
        byte[] pdf = resumeService.exportPdf(title, request.getContent());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume-export.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
