package com.sumitra.resume.service;

import com.sumitra.resume.dto.ChatHistoryResponse;

import java.util.List;

public interface HistoryService {

    List<ChatHistoryResponse> findByResumeId(String resumeId, String userEmail);

    List<ChatHistoryResponse> findAll(String userEmail);

    void deleteById(Long id, String userEmail);
}
