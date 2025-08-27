package com.tripcut.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private UserResponseDto.JoinResultDTO user;
    private String accessToken;
    private String refreshToken;
    private Boolean firstLogin;
}