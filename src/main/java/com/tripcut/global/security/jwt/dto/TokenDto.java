package com.tripcut.global.security.jwt.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class TokenDto {


    private String accessToken;
    private String refreshToken;


    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}