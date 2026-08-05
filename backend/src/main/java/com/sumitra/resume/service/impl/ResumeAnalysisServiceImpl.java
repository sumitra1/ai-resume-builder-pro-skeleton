package com.sumitra.resume.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumitra.resume.ai.GeminiService;
import com.sumitra.resume.dto.ResumeAnalysisResponse;
import com.sumitra.resume.entity.Resume;
import com.sumitra.resume.repository.ResumeRepository;
import com.sumitra.resume.service.ResumeAnalysisService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ResumeAnalysisServiceImpl 
        implements ResumeAnalysisService {



    private final ResumeRepository resumeRepository;

    private final GeminiService geminiService;

    private final ObjectMapper objectMapper;



    @Override
    public ResumeAnalysisResponse analyzeResume(
            String resumeId,
            String email
    ) {


        Resume resume =
                resumeRepository.findById(
                        Long.parseLong(resumeId)
                )
                .orElseThrow(
                    () -> new RuntimeException("Resume not found")
                );



        String prompt =
                """
                Analyze this resume.

                Return ONLY JSON.

                Format:

                {
                  "score": 0,
                  "strengths": [],
                  "weaknesses": [],
                  "suggestions": []
                }


                Resume:

                %s

                """
                .formatted(
                    resume.getResumeText()
                );



        String response =
                geminiService.generate(prompt);



        try {

            return objectMapper.readValue(
                    response,
                    ResumeAnalysisResponse.class
            );


        } catch(Exception e){

            throw new RuntimeException(
                "Failed to parse AI response"
            );

        }

    }

}