package com.sumitra.resume.dto;

import java.util.List;

public class JobMatchResponse {

    private Integer matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String notes;

    public JobMatchResponse() {
    }

    public JobMatchResponse(Integer matchScore, List<String> matchedSkills, List<String> missingSkills, String notes) {
        this.matchScore = matchScore;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.notes = notes;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
