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
    private UserResponseDto.JoinResultDTO user; // 가입/로그인 결과(이미 갖고 계신 DTO)
    private String accessToken;                 // 우리 서비스용 AT (JWT)
    private String refreshToken;                // 우리 서비스용 RT (JWT)
}