package com.sumitra.resume.service.chroma;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChromaServiceImpl implements ChromaService {

    private final RestTemplate restTemplate;
    private final String chromaEndpoint;

    public ChromaServiceImpl(RestTemplate restTemplate,
                             @Value("${chroma.endpoint}") String chromaEndpoint) {
        this.restTemplate = restTemplate;
        this.chromaEndpoint = chromaEndpoint;
    }

    @Override
    public void createCollection() {
        String url = chromaEndpoint
                + "/api/v2/tenants/default_tenant/databases/default_database/collections";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("name", "resume_chunks");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            var response = restTemplate.postForEntity(url, request, String.class);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

