package com.tripcut.domain.user.service;

import com.tripcut.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    User oAuthLogin(String accessCode, HttpServletResponse httpServletResponse);
}