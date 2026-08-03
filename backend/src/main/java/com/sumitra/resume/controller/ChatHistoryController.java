package com.sumitra.resume.controller;

import com.sumitra.resume.dto.ChatHistoryResponse;
import com.sumitra.resume.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat/history")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final HistoryService historyService;

    @GetMapping
    public List<ChatHistoryResponse> findAll(Authentication authentication) {
        return historyService.findAll(authentication.getName());
    }

    @GetMapping("/{resumeId}")
    public List<ChatHistoryResponse> findByResumeId(Authentication authentication, @PathVariable String resumeId) {
        return historyService.findByResumeId(resumeId, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteById(Authentication authentication, @PathVariable Long id) {
        historyService.deleteById(id, authentication.getName());
    }
}
