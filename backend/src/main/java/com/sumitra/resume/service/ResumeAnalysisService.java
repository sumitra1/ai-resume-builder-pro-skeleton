package com.sumitra.resume.service;

import com.sumitra.resume.dto.ResumeAnalysisResponse;


public interface ResumeAnalysisService {


    ResumeAnalysisResponse analyzeResume(
            String resumeId,
            String email
    );

}