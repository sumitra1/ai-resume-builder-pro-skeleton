package com.sumitra.resume.service.impl;

import com.sumitra.resume.ai.GeminiService;
import com.sumitra.resume.dto.ImproveResumeResponse;
import com.sumitra.resume.dto.SourceChunk;
import com.sumitra.resume.service.ResumeImproveService;
import com.sumitra.resume.service.embedding.EmbeddingService;
import com.sumitra.resume.service.vectorstore.ChromaSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeImproveServiceImpl implements ResumeImproveService {

    private final ChromaSearchService chromaSearchService;
    private final EmbeddingService embeddingService;
    private final GeminiService geminiService;

    @Override
    public ImproveResumeResponse improve(String resumeId, String section) {
        List<Float> embedding = embeddingService.generateEmbedding(section);
        List<SourceChunk> chunks = chromaSearchService.searchChunks(embedding, resumeId, 5);

        String context = chunks.stream()
                .map(SourceChunk::getText)
                .reduce((a, b) -> a + "\n\n" + b)
                .orElse("");

        String prompt = buildPrompt(context, section);
        String answer = geminiService.generate(prompt);

        return new ImproveResumeResponse(answer);
    }

    private String buildPrompt(String context, String section) {
        return """
                You are an expert resume writer.

                Improve the following resume section using the resume context when relevant.

                Rules:
                - Make it ATS friendly.
                - Use strong action verbs.
                - Add measurable impact when possible.
                - Do not invent companies, projects, or skills not supported by context.
                - Keep it professional.
                - Return bullet points only.

                Resume Context:
                %s

                Section to Improve:
                %s

                Improved Version:
                """.formatted(context, section);
    }
}
