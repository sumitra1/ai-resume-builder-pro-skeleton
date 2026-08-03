package com.sumitra.resume.service;

import com.sumitra.resume.dto.ChatResponse;

public interface ChatService {

    ChatResponse ask(String resumeId, String question, String userEmail);

}
