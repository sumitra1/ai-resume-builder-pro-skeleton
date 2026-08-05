package com.sumitra.resume.dto;

public class ExportPdfRequest {

    private String content;
    private String title;

    public ExportPdfRequest() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
