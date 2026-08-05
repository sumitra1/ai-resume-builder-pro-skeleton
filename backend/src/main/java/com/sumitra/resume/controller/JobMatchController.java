package com.sumitra.resume.controller;

import com.sumitra.resume.dto.JobMatchRequest;
import com.sumitra.resume.dto.JobMatchResponse;
import com.sumitra.resume.service.JobMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-match")
@RequiredArgsConstructor
public class JobMatchController {

    private final JobMatchService jobMatchService;

    @PostMapping
    public JobMatchResponse match(Authentication authentication, @RequestBody JobMatchRequest request) {
        if (request.getResumeId() == null || request.getResumeId().isBlank()) {
            throw new IllegalArgumentException("resumeId is required");
        }
        if (request.getJobDescription() == null || request.getJobDescription().isBlank()) {
            throw new IllegalArgumentException("jobDescription is required");
        }
        return jobMatchService.match(request, authentication.getName());
    }
}
