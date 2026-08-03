package com.sumitra.resume.dto;

public class ChatRequest {

    private String resumeId;
    private String question;

    public ChatRequest() {
    }

    public ChatRequest(String resumeId, String question) {
        this.resumeId = resumeId;
        this.question = question;
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
}
