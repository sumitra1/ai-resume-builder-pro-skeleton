package com.sumitra.resume.service;

import com.sumitra.resume.dto.ImproveResumeResponse;


public interface ResumeImproveService {


    ImproveResumeResponse improve(
            String resumeId,
            String section
    );


}