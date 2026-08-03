package com.sumitra.resume.dto;

public class SourceChunk {

    private String chunkId;
    private Double score;
    private String text;

    public SourceChunk() {
    }

    public SourceChunk(String chunkId, Double score, String text) {
        this.chunkId = chunkId;
        this.score = score;
        this.text = text;
    }

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
