package com.sumitra.resume.controller;


import com.sumitra.resume.dto.ResumeAnalysisResponse;
import com.sumitra.resume.service.ResumeAnalysisService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeAnalysisController {



    private final ResumeAnalysisService resumeAnalysisService;



    @PostMapping("/analyze/{resumeId}")
    public ResumeAnalysisResponse analyze(
            @PathVariable String resumeId,
            Authentication authentication
    ){


        return resumeAnalysisService.analyzeResume(
                resumeId,
                authentication.getName()
        );

    }

}