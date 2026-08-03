package com.sumitra.resume.service.vectorstore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChromaSearchService {

    private final RestTemplate restTemplate;
    private final String chromaEndpoint;
    private final String collectionId;

    public ChromaSearchService(
            RestTemplate restTemplate,
            @Value("${chroma.endpoint}") String chromaEndpoint,
            @Value("${chroma.collection.id}") String collectionId
    ) {
        this.restTemplate = restTemplate;
        this.chromaEndpoint = chromaEndpoint;
        this.collectionId = collectionId;
    }

    public String search(List<Float> embedding) {

        String url = chromaEndpoint
                + "/api/v2/tenants/default_tenant/databases/default_database/collections/"
                + collectionId
                + "/query";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("query_embeddings", List.of(embedding));
        body.put("n_results", 3);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, request, String.class);
    }
}
