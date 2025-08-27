package com.tripcut.domain.user.service;

import com.tripcut.domain.user.dto.response.AuthLoginResult;
import com.tripcut.domain.user.dto.response.KakaoTokenResponse;

public interface AuthService {
    AuthLoginResult kakaoLoginWithOAuthToken(KakaoTokenResponse token);
}