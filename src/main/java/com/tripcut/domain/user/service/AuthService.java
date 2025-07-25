package com.tripcut.domain.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tripcut.core.security.jwt.dto.LoginRequest;
import com.tripcut.core.security.jwt.dto.SignupRequest;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import com.tripcut.core.security.jwt.aggregate.JwtTokenProvider;
import com.tripcut.core.security.jwt.dto.LoginResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setPreferredLanguage(signupRequest.getPreferredLanguage());
        user.setPreferredGenres(signupRequest.getPreferredGenres());

        userRepository.save(user);

        // 회원가입 후 바로 Access/Refresh 토큰 발급
        String accessToken = tokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());
        LoginResponse response = new LoginResponse(
            accessToken,
            refreshToken,
            "Bearer",
            tokenProvider.getAccessTokenExpiration(),
            tokenProvider.getRefreshTokenExpiration()
        );
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.generateAccessToken(loginRequest.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(loginRequest.getEmail());
        LoginResponse response = new LoginResponse(
            accessToken,
            refreshToken,
            "Bearer",
            tokenProvider.getAccessTokenExpiration(),
            tokenProvider.getRefreshTokenExpiration()
        );
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<LoginResponse> login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(email);
        String refreshToken = tokenProvider.generateRefreshToken(email);

        LoginResponse response = new LoginResponse(
            accessToken,
            refreshToken,
            "Bearer",
            tokenProvider.getAccessTokenExpiration(),
            tokenProvider.getRefreshTokenExpiration()
        );

        return ResponseEntity.ok(response);
    }
} 