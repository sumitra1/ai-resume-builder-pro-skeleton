package com.sumitra.resume.service.embedding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    private final GoogleAiEmbeddingModel embeddingModel;

    public EmbeddingServiceImpl(
            @Value("${gemini.api.key}") String apiKey) {

        this.embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-embedding-001")
                .build();
    }

    @Override
    public List<Float> generateEmbedding(String text) {

        Embedding embedding =
                embeddingModel.embed(text).content();

        return embedding.vectorAsList();
    }
}
