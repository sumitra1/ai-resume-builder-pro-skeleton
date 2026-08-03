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
        return chatService.ask(request.getResumeId(), request.getQuestion(), authentication.getName());
    }
}
