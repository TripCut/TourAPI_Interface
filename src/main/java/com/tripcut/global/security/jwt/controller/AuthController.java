package com.tripcut.global.security.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripcut.core.annotation.Logging;
import com.tripcut.core.annotation.Security;
import com.tripcut.core.annotation.Validation;
import com.tripcut.domain.user.service.AuthService;
import com.tripcut.global.security.jwt.dto.LoginRequest;
import com.tripcut.global.security.jwt.dto.LoginResponse;
import com.tripcut.global.security.jwt.dto.SignupRequest;
import com.tripcut.global.security.jwt.dto.TokenResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Logging(level = "INFO", includeArgs = true, includeResult = true)
    @Security(roles = {"USER"}, requireAuth = false)
    @Validation(validateNull = true, validateEmpty = true, requiredFields = {"email", "password", "username"})
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.registerUser(signupRequest);
    }

    @PostMapping("/login")
    @Logging(level = "INFO", includeArgs = true)
    @Security(roles = {"USER"}, requireAuth = false)
    @Validation(validateNull = true, validateEmpty = true, requiredFields = {"email", "password"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/refresh")
    @Logging(level = "INFO", includeArgs = true)
    @Security(roles = {"USER"}, requireAuth = false)
    @Validation(validateNull = true, validateEmpty = true)
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
} 