package com.sumitra.resume.service.vectorstore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumitra.resume.dto.SourceChunk;
import com.sumitra.resume.service.chroma.ChromaCollectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChromaSearchService {

    private final RestTemplate restTemplate;
    private final String chromaEndpoint;
    private final ChromaCollectionService chromaCollectionService;
    private final ObjectMapper objectMapper;

    public ChromaSearchService(
            RestTemplate restTemplate,
            @Value("${chroma.endpoint}") String chromaEndpoint,
            ChromaCollectionService chromaCollectionService,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.chromaEndpoint = chromaEndpoint;
        this.chromaCollectionService = chromaCollectionService;
        this.objectMapper = objectMapper;
    }

    public String search(List<Float> embedding) {
        String url = queryUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("query_embeddings", List.of(embedding));
        body.put("n_results", 3);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, request, String.class);
    }

    public List<SourceChunk> searchChunks(List<Float> embedding, String resumeId, int nResults) {
        chromaCollectionService.ensureCollectionExists();
        String url = queryUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("query_embeddings", List.of(embedding));
        body.put("n_results", nResults);
        body.put("include", List.of(
                "documents",
                "metadatas",
                "distances"
        ));

        if (resumeId != null && !resumeId.isBlank()) {
            body.put("where", Map.of("resumeId", resumeId));
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String rawResponse = restTemplate.postForObject(url, request, String.class);
        return parseChunks(rawResponse);
    }

    private String queryUrl() {
        return chromaEndpoint
                + "/api/v2/tenants/default_tenant/databases/default_database/collections/"
                + chromaCollectionService.getCollectionId()
                + "/query";
    }

    private List<SourceChunk> parseChunks(String rawResponse) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return List.of();
        }

        try {
            Map<String, Object> response = objectMapper.readValue(rawResponse, new TypeReference<>() {
            });

            List<List<String>> documentLists = extractDocumentLists(response);
            List<List<Double>> distanceLists = extractDoubleLists(response);

            if (documentLists.isEmpty()) {
                return List.of();
            }

            List<String> documents = documentLists.get(0);
            List<Double> distances = distanceLists.isEmpty() ? List.of() : distanceLists.get(0);

            List<SourceChunk> chunks = new ArrayList<>();
            for (int i = 0; i < documents.size(); i++) {
                String chunkId = null;
                Double score = i < distances.size() ? distances.get(i) : null;
                chunks.add(new SourceChunk(chunkId, score, documents.get(i)));
            }

            return chunks;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Chroma search response", e);
        }
    }

    private List<List<String>> extractStringLists(Map<String, Object> response, String key) {
        if (response.containsKey(key)) {
            return convertToNestedStrings(response.get(key));
        }

        if (response.containsKey("results")) {
            Object resultsValue = response.get("results");
            List<Map<String, Object>> resultEntries = objectMapper.convertValue(resultsValue, new TypeReference<>() {
            });
            for (Map<String, Object> entry : resultEntries) {
                if (entry.containsKey(key)) {
                    return convertToNestedStrings(entry.get(key));
                }
            }
        }

        return List.of();
    }

    private List<List<Double>> extractDoubleLists(Map<String, Object> response) {
        if (response.containsKey("distances")) {
            return convertToNestedDoubles(response.get("distances"));
        }

        if (response.containsKey("results")) {
            Object resultsValue = response.get("results");
            List<Map<String, Object>> resultEntries = objectMapper.convertValue(resultsValue, new TypeReference<>() {
            });
            for (Map<String, Object> entry : resultEntries) {
                if (entry.containsKey("distances")) {
                    return convertToNestedDoubles(entry.get("distances"));
                }
            }
        }

        return List.of();
    }

    private List<List<Double>> convertToNestedDoubles(Object rawValue) {
        try {
            return objectMapper.convertValue(rawValue, new TypeReference<>() {
            });
        } catch (IllegalArgumentException ignored) {
        }

        try {
            List<Number> flat = objectMapper.convertValue(rawValue, new TypeReference<>() {
            });
            return List.of(flat.stream().map(Number::doubleValue).toList());
        } catch (IllegalArgumentException ignored) {
        }

        return List.of();
    }

    private List<List<String>> extractDocumentLists(Map<String, Object> response) {
        if (response.containsKey("documents")) {
            return convertToNestedStrings(response.get("documents"));
        }

        if (response.containsKey("results")) {
            Object resultsValue = response.get("results");
            List<Map<String, Object>> resultEntries = objectMapper.convertValue(resultsValue, new TypeReference<>() {
            });
            for (Map<String, Object> entry : resultEntries) {
                if (entry.containsKey("documents")) {
                    return convertToNestedStrings(entry.get("documents"));
                }
            }
        }

        return List.of();
    }

    private List<List<Map<String, Object>>> extractMetadataLists(Map<String, Object> response) {
        if (response.containsKey("metadatas")) {
            return objectMapper.convertValue(response.get("metadatas"), new TypeReference<>() {
            });
        }

        if (response.containsKey("results")) {
            Object resultsValue = response.get("results");
            List<Map<String, Object>> resultEntries = objectMapper.convertValue(resultsValue, new TypeReference<>() {
            });
            for (Map<String, Object> entry : resultEntries) {
                if (entry.containsKey("metadatas")) {
                    return objectMapper.convertValue(entry.get("metadatas"), new TypeReference<>() {
                    });
                }
            }
        }

        return List.of();
    }

    private List<List<String>> convertToNestedStrings(Object rawValue) {
        try {
            return objectMapper.convertValue(rawValue, new TypeReference<>() {
            });
        } catch (IllegalArgumentException ignored) {
        }

        try {
            List<String> flat = objectMapper.convertValue(rawValue, new TypeReference<>() {
            });
            return List.of(flat);
        } catch (IllegalArgumentException ignored) {
        }

        return List.of();
    }
}
