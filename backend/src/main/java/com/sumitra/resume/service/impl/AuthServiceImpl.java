package com.sumitra.resume.service.impl;

import com.sumitra.resume.dto.RegisterRequest;
import com.sumitra.resume.entity.User;
import com.sumitra.resume.repository.UserRepository;
import com.sumitra.resume.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

  @Override
public String register(RegisterRequest request) {

    System.out.println("========== REGISTER API ==========");
    System.out.println("Request: " + request);

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email already exists");
    }

    User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(request.getPassword())
            .createdAt(LocalDateTime.now())
            .build();

    System.out.println("User Object: " + user);

    userRepository.save(user);

    System.out.println("User saved successfully!");

    return "User registered successfully";
}
}