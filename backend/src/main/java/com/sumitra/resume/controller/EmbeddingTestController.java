package com.sumitra.resume.controller;

import com.sumitra.resume.service.embedding.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class EmbeddingTestController {

    private final EmbeddingService embeddingService;

    @GetMapping("/embedding")
    public int test() {
        return embeddingService.generateEmbedding("Hello Chroma").size();
    }
}
