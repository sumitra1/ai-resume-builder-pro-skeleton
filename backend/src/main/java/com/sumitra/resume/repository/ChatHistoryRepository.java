package com.sumitra.resume.repository;

import com.sumitra.resume.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findByUserEmailAndResumeId(String email, String resumeId);

    List<ChatHistory> findByUserEmail(String email);
}
