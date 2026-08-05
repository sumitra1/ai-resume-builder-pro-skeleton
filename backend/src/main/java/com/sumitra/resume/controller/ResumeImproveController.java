package com.sumitra.resume.controller;


import com.sumitra.resume.dto.ImproveResumeRequest;
import com.sumitra.resume.dto.ImproveResumeResponse;
import com.sumitra.resume.service.ResumeImproveService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeImproveController {



    private final ResumeImproveService resumeImproveService;




    @PostMapping("/improve")
    public ImproveResumeResponse improve(
            @RequestBody ImproveResumeRequest request
    ){


        return resumeImproveService.improve(
                request.getResumeId(),
                request.getSection()
        );

    }


}