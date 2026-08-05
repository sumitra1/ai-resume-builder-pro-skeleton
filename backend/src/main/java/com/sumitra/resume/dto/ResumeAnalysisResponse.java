package com.sumitra.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {

    private int score;

    private List<String> strengths;

    private List<String> weaknesses;

    private List<String> suggestions;

}