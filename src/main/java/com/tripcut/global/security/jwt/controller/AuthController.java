package com.tripcut.global.security.jwt.controller;

import com.tripcut.global.security.jwt.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;


@RestController
@RequestMapping(BASE_URL)
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = tokenProvider.regenerateAccessToken(refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}