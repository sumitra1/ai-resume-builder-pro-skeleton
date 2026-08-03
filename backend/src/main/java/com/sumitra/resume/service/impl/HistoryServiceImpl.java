package com.sumitra.resume.service.impl;

import com.sumitra.resume.dto.ChatHistoryResponse;
import com.sumitra.resume.entity.ChatHistory;
import com.sumitra.resume.repository.ChatHistoryRepository;
import com.sumitra.resume.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    @Override
    public List<ChatHistoryResponse> findByResumeId(String resumeId, String userEmail) {
        return chatHistoryRepository.findByUserEmailAndResumeId(userEmail, resumeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatHistoryResponse> findAll(String userEmail) {
        return chatHistoryRepository.findByUserEmail(userEmail).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id, String userEmail) {
        ChatHistory history = chatHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat history not found"));

        if (!history.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to delete this chat history");
        }

        chatHistoryRepository.delete(history);
    }

    private ChatHistoryResponse toResponse(ChatHistory history) {
        return new ChatHistoryResponse(
                history.getId(),
                history.getResumeId(),
                history.getQuestion(),
                history.getAnswer(),
                history.getCreatedAt()
        );
    }
}
