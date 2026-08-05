package com.sumitra.resume.controller;

import com.sumitra.resume.dto.ChatRequest;
import com.sumitra.resume.dto.ChatResponse;
import com.sumitra.resume.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(Authentication authentication, @RequestBody ChatRequest request) {
        if (request.getResumeId() == null || request.getResumeId().isBlank()) {
            throw new IllegalArgumentException("resumeId is required");
        }
        if (request.getQuestion() == null || request.getQuestion().isBlank()) {
            throw new IllegalArgumentException("question is required");
        }
        return chatService.ask(request.getResumeId(), request.getQuestion(), authentication.getName());
    }
}
