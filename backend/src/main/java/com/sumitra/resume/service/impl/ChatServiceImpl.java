package com.sumitra.resume.service.impl;

import com.sumitra.resume.ai.GeminiService;
import com.sumitra.resume.dto.ChatResponse;
import com.sumitra.resume.dto.SourceChunk;
import com.sumitra.resume.entity.ChatHistory;
import com.sumitra.resume.entity.User;
import com.sumitra.resume.repository.ChatHistoryRepository;
import com.sumitra.resume.repository.UserRepository;
import com.sumitra.resume.service.ChatService;
import com.sumitra.resume.service.embedding.EmbeddingService;
import com.sumitra.resume.service.vectorstore.ChromaSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChromaSearchService chromaSearchService;
    private final EmbeddingService embeddingService;
    private final GeminiService geminiService;
    private final UserRepository userRepository;
    private final ChatHistoryRepository chatHistoryRepository;

    @Override
    public ChatResponse ask(String resumeId, String question, String userEmail) {
        List<Float> questionEmbedding = embeddingService.generateEmbedding(question);
        List<SourceChunk> sourceChunks = chromaSearchService.searchChunks(questionEmbedding, resumeId, 5);

        String context = sourceChunks.stream()
                .map(SourceChunk::getText)
                .reduce((a, b) -> a + "\n\n" + b)
                .orElse("");

        System.out.println("===== Retrieved Context =====");
        System.out.println(context);
        System.out.println("=============================");

        String prompt = buildPrompt(context, question);
        String answer = geminiService.generate(prompt);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        ChatHistory history = new ChatHistory();
        history.setUser(user);
        history.setResumeId(resumeId);
        history.setQuestion(question);
        history.setAnswer(answer);
        history.setCreatedAt(LocalDateTime.now());
        chatHistoryRepository.save(history);

        return new ChatResponse(answer, sourceChunks);
    }

    private String buildPrompt(String context, String question) {
        return "You are an AI Resume Assistant.\n\n"
                + "Answer ONLY using the resume context below.\n\n"
                + "If the answer is not available, reply:\n"
                + "\"The resume does not mention this.\"\n\n"
                + "Resume Context:\n"
                + context + "\n\n"
                + "Question:\n"
                + question + "\n\n"
                + "Answer:";
    }
}
