package com.sumitra.resume.dto;

import java.time.LocalDateTime;

public class ChatHistoryResponse {

    private Long id;
    private String resumeId;
    private String question;
    private String answer;
    private LocalDateTime createdAt;

    public ChatHistoryResponse() {
    }

    public ChatHistoryResponse(Long id, String resumeId, String question, String answer, LocalDateTime createdAt) {
        this.id = id;
        this.resumeId = resumeId;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
