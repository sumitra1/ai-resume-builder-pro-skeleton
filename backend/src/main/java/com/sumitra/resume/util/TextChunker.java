package com.sumitra.resume.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    public List<String> chunkText(String text) {
        List<String> chunks = new ArrayList<>();

        int chunkSize = 500;

        for (int i = 0; i < text.length(); i += chunkSize) {
            int end = Math.min(text.length(), i + chunkSize);
            chunks.add(text.substring(i, end));
        }

        return chunks;
    }
}
