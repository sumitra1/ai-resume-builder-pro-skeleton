package com.sumitra.resume.service.vectorstore;

import com.sumitra.resume.model.ResumeChunk;
import com.sumitra.resume.service.chroma.ChromaCollectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ChromaVectorStoreService {

    private final RestTemplate restTemplate;
    private final String chromaEndpoint;
    private final ChromaCollectionService chromaCollectionService;

    public ChromaVectorStoreService(
            RestTemplate restTemplate,
            @Value("${chroma.endpoint}") String chromaEndpoint,
            ChromaCollectionService chromaCollectionService
    ) {
        this.restTemplate = restTemplate;
        this.chromaEndpoint = chromaEndpoint;
        this.chromaCollectionService = chromaCollectionService;
    }

    public void upsertResumeChunk(ResumeChunk chunk) {
        chromaCollectionService.ensureCollectionExists();

        String url = chromaEndpoint
                + "/api/v2/tenants/default_tenant/databases/default_database/collections/"
                + chromaCollectionService.getCollectionId()
                + "/add";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String id = chunk.getId() == null ? UUID.randomUUID().toString() : chunk.getId();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("resumeId", chunk.getResumeId());

        Map<String, Object> body = new HashMap<>();
        body.put("ids", List.of(id));
        body.put("documents", List.of(chunk.getContent()));
        body.put("embeddings", List.of(chunk.getEmbedding()));
        body.put("metadatas", List.of(metadata));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}
