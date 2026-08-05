package com.sumitra.resume.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumitra.resume.ai.GeminiService;
import com.sumitra.resume.dto.JobMatchRequest;
import com.sumitra.resume.dto.JobMatchResponse;
import com.sumitra.resume.entity.Resume;
import com.sumitra.resume.repository.ResumeRepository;
import com.sumitra.resume.service.JobMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobMatchServiceImpl implements JobMatchService {

    private final ResumeRepository resumeRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    @Override
    public JobMatchResponse match(JobMatchRequest request, String userEmail) {
        Resume resume = resumeRepository.findById(Long.parseLong(request.getResumeId()))
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to access this resume");
        }

        String prompt = buildPrompt(resume.getResumeText(), request.getJobDescription());
        String response = geminiService.generate(prompt);

        try {
            return objectMapper.readValue(extractJson(response), JobMatchResponse.class);
        } catch (Exception e) {
            return new JobMatchResponse(0, List.of(), List.of(), response);
        }
    }

    private String buildPrompt(String resumeText, String jobDescription) {
        return """
                Compare this resume against the job description.

                Return ONLY valid JSON with this exact structure:
                {
                  "matchScore": 0,
                  "matchedSkills": [],
                  "missingSkills": [],
                  "notes": ""
                }

                Resume:
                %s

                Job Description:
                %s
                """.formatted(resumeText, jobDescription);
    }

    private String extractJson(String response) {
        String trimmed = response.trim();
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('\n');
            int end = trimmed.lastIndexOf("```");
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start + 1, end).trim();
            }
        }
        return trimmed;
    }
}
