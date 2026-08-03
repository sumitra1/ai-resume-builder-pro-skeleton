package com.sumitra.resume.controller;

import com.sumitra.resume.service.chroma.ChromaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ChromaService chromaService;

    @GetMapping("/chroma")
    public String test() {
        chromaService.createCollection();
        return "Collection Created";
    }
}
