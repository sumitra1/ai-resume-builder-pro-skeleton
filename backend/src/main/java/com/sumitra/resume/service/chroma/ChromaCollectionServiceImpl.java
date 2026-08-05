package com.sumitra.resume.service.chroma;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChromaCollectionServiceImpl implements ChromaCollectionService {

    private static final Logger log = LoggerFactory.getLogger(ChromaCollectionServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String chromaEndpoint;
    private final String collectionName;

    private volatile String collectionId;

    public ChromaCollectionServiceImpl(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${chroma.endpoint}") String chromaEndpoint,
            @Value("${chroma.collection.name:resume_chunks}") String collectionName
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.chromaEndpoint = chromaEndpoint;
        this.collectionName = collectionName;
    }

    @Override
    public String getCollectionId() {
        if (collectionId == null) {
            ensureCollectionExists();
        }
        return collectionId;
    }

    @Override
    public synchronized void ensureCollectionExists() {
        if (collectionId != null) {
            return;
        }

        String existingId = findCollectionIdByName();
        if (existingId != null) {
            collectionId = existingId;
            log.info("Using existing Chroma collection '{}' (id={})", collectionName, collectionId);
            return;
        }

        collectionId = createCollection();
        log.info("Created Chroma collection '{}' (id={})", collectionName, collectionId);
    }

    private String findCollectionIdByName() {
        String url = collectionsUrl();

        try {
            String rawResponse = restTemplate.getForObject(url, String.class);
            if (rawResponse == null || rawResponse.isBlank()) {
                return null;
            }

            List<Map<String, Object>> collections = objectMapper.readValue(
                    rawResponse,
                    new TypeReference<>() {}
            );

            for (Map<String, Object> collection : collections) {
                Object name = collection.get("name");
                if (name != null && collectionName.equals(name.toString())) {
                    Object id = collection.get("id");
                    return id != null ? id.toString() : null;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to list Chroma collections: {}", e.getMessage());
        }

        return null;
    }

    private String createCollection() {
        String url = collectionsUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("name", collectionName);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String rawResponse = restTemplate.postForObject(url, request, String.class);
            Map<String, Object> response = objectMapper.readValue(
                    rawResponse,
                    new TypeReference<>() {}
            );
            Object id = response.get("id");
            if (id == null) {
                throw new RuntimeException("Chroma did not return a collection id");
            }
            return id.toString();
        } catch (HttpClientErrorException.Conflict e) {
            String existingId = findCollectionIdByName();
            if (existingId != null) {
                return existingId;
            }
            throw new RuntimeException("Chroma collection conflict but id not found", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Chroma collection", e);
        }
    }

    private String collectionsUrl() {
        return chromaEndpoint
                + "/api/v2/tenants/default_tenant/databases/default_database/collections";
    }
}
