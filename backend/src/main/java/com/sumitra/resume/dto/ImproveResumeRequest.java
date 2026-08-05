package com.sumitra.resume.dto;


public class ImproveResumeRequest {

    private String resumeId;

    private String section;


    public ImproveResumeRequest() {
    }


    public ImproveResumeRequest(
            String resumeId,
            String section
    ) {
        this.resumeId = resumeId;
        this.section = section;
    }


    public String getResumeId() {
        return resumeId;
    }


    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }


    public String getSection() {
        return section;
    }


    public void setSection(String section) {
        this.section = section;
    }
}