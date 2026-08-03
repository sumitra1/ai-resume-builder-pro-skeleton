package com.sumitra.resume.service;

import com.sumitra.resume.model.ResumeChunk;
import com.sumitra.resume.service.embedding.EmbeddingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeEmbeddingService {

    private final EmbeddingService embeddingService;

    public ResumeEmbeddingService(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    public ResumeChunk createChunk(String text) {
        List<Float> vector = embeddingService.generateEmbedding(text);

        ResumeChunk chunk = new ResumeChunk();
        chunk.setContent(text);
        chunk.setEmbedding(vector);

        return chunk;
    }
}
