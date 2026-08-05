package com.sumitra.resume.service.impl;


import com.sumitra.resume.ai.GeminiService;
import com.sumitra.resume.dto.ImproveResumeResponse;
import com.sumitra.resume.dto.SourceChunk;
import com.sumitra.resume.service.ResumeImproveService;
import com.sumitra.resume.service.vectorstore.ChromaSearchService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ResumeImproveServiceImpl 
        implements ResumeImproveService {


    private final ChromaSearchService chromaSearchService;

    private final GeminiService geminiService;



    @Override
    public ImproveResumeResponse improve(
            String resumeId,
            String section
    ) {



        String searchText =
                section;



        /*
          We need embedding for search.
          Currently your ChromaSearchService
          already expects embeddings.
          
          For now use existing flow:
          generate embedding of section text
        */


        String prompt =
                buildPrompt(
                        section
                );


        String answer =
                geminiService.generate(prompt);



        return new ImproveResumeResponse(answer);

    }




    private String buildPrompt(
            String section
    ){


        return """
                
                You are an expert resume writer.

                Improve the following resume section.

                Rules:
                - Make it ATS friendly.
                - Use strong action verbs.
                - Add measurable impact if possible.
                - Keep it professional.
                - Return bullet points only.


                Resume Section:

                %s


                Improved Version:

                """.formatted(section);


    }

}