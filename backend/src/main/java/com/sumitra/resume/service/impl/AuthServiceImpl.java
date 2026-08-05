package com.sumitra.resume.service.impl;

import com.sumitra.resume.dto.AuthResponse;
import com.sumitra.resume.dto.LoginRequest;
import com.sumitra.resume.dto.RegisterRequest;
import com.sumitra.resume.entity.User;
import com.sumitra.resume.exception.EmailAlreadyExistsException;
import com.sumitra.resume.exception.InvalidCredentialsException;
import com.sumitra.resume.exception.UserNotFoundException;
import com.sumitra.resume.repository.UserRepository;
import com.sumitra.resume.security.JwtService;
import com.sumitra.resume.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }
}
