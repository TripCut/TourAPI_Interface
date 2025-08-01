package com.tripcut.domain.user.service.impl;

import com.tripcut.domain.user.dto.KakaoDto;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import com.tripcut.domain.user.service.AuthService;
import com.tripcut.domain.user.util.AuthConverter;
import com.tripcut.domain.user.util.JwtUtil;
import com.tripcut.domain.user.util.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {
        KakaoDto.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDto.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);

        Optional<User> queryUser = userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail());

        if (queryUser.isPresent()) {
            User user = queryUser.get();
            httpServletResponse.setHeader("Authorization", jwtUtil.createAccessToken(user.getEmail(), "ROLE_USER"));
            return user;
        } else {
            User user = AuthConverter.toUser(kakaoProfile.getKakao_account().getEmail(),
                    kakaoProfile.getKakao_account().getProfile().getNickname(),
                    "1234",
                    passwordEncoder);
            userRepository.save(user);
            httpServletResponse.setHeader("Authorization", jwtUtil.createAccessToken(user.getEmail(), String.valueOf(user.getRole())));
            return user;
        }
    }
}
