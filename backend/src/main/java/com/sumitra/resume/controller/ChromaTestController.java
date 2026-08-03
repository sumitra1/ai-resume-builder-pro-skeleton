package com.sumitra.resume.controller;

import com.sumitra.resume.model.ResumeChunk;
import com.sumitra.resume.service.embedding.EmbeddingService;
import com.sumitra.resume.service.vectorstore.ChromaVectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/chroma")
@RequiredArgsConstructor
public class ChromaTestController {

    private final EmbeddingService embeddingService;
    private final ChromaVectorStoreService chromaVectorStoreService;

    @PostMapping
    public String test() {
        ResumeChunk chunk = new ResumeChunk();
        chunk.setResumeId("resume-1");
        chunk.setContent("Java developer with Spring Boot and React experience");
        chunk.setEmbedding(
                embeddingService.generateEmbedding(chunk.getContent())
        );

        chromaVectorStoreService.upsertResumeChunk(chunk);

        return "stored";
    }
}
