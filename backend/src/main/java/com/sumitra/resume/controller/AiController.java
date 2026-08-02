package com.sumitra.resume.controller;

import com.sumitra.resume.ai.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final GeminiService geminiService;

    @GetMapping("/test")
    public String test(@RequestParam String prompt) {
        return geminiService.ask(prompt);
    }
}
