package com.tripcut.domain.user.dto.response;

import com.tripcut.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResult {
    private final User user;
    private final String accessToken;
    private final String refreshToken;
}
