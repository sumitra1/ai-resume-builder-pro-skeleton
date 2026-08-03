package com.sumitra.resume.model;

import lombok.Data;

import java.util.List;

@Data
public class ResumeChunk {

    private String id;

    private String resumeId;

    private String content;

    private List<Float> embedding;
}
