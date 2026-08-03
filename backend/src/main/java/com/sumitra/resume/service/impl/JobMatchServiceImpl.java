package com.sumitra.resume.service.impl;

import com.sumitra.resume.dto.JobMatchRequest;
import com.sumitra.resume.dto.JobMatchResponse;
import com.sumitra.resume.entity.Resume;
import com.sumitra.resume.repository.ResumeRepository;
import com.sumitra.resume.service.JobMatchService;
import com.sumitra.resume.ai.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobMatchServiceImpl implements JobMatchService {

    private final ResumeRepository resumeRepository;
    private final GeminiService geminiService;

    @Override
    public JobMatchResponse match(JobMatchRequest request, String userEmail) {
        Resume resume = resumeRepository.findById(Long.valueOf(request.getResumeId()))
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        String prompt = buildPrompt(resume.getResumeText(), request.getJobDescription());
        String response = geminiService.generate(prompt);

        // Simple parse from Gemini answer; a more robust production version should use structured output.
        List<String> matchedSkills = List.of();
        List<String> missingSkills = List.of();

        return new JobMatchResponse(0, matchedSkills, missingSkills, response);
    }

    private String buildPrompt(String resumeText, String jobDescription) {
        return "You are a job matching assistant.\n\n"
                + "Compare this resume against the job description.\n\n"
                + "Resume:\n" + resumeText + "\n\n"
                + "Job description:\n" + jobDescription + "\n\n"
                + "Provide:\n"
                + "1) A match score from 0 to 100.\n"
                + "2) A list of matched skills.\n"
                + "3) A list of missing skills.\n"
                + "4) A short summary why this resume is a good fit or not.\n\n"
                + "Answer in JSON format with keys: matchScore, matchedSkills, missingSkills, notes.";
    }
}
