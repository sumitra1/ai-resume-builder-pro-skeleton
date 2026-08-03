package com.sumitra.resume.ai;

import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final ChatModel chatModel;

    public String ask(String prompt) {
        return chatModel.chat(prompt);
    }

    public String generate(String prompt) {
        return chatModel.chat(prompt);
    }
}
