package com.sumitra.resume.controller;

import com.sumitra.resume.service.embedding.EmbeddingService;
import com.sumitra.resume.service.vectorstore.ChromaSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/search")
@RequiredArgsConstructor
public class SearchTestController {

    private final EmbeddingService embeddingService;
    private final ChromaSearchService searchService;

    @GetMapping
    public String search() {
        String question = "Does this developer know React?";

        List<Float> vector = embeddingService.generateEmbedding(question);

        return searchService.search(vector);
    }
}
