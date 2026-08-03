package com.sumitra.resume.service.embedding;

import java.util.List;

public interface EmbeddingService {

    List<Float> generateEmbedding(String text);

}
