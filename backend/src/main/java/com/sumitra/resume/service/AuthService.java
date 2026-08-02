package com.sumitra.resume.service;

import com.sumitra.resume.dto.AuthResponse;
import com.sumitra.resume.dto.LoginRequest;
import com.sumitra.resume.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}