package com.tripcut.domain.user.service.impl;

import com.tripcut.domain.user.dto.KakaoDto;
import com.tripcut.domain.user.dto.response.AuthLoginResult;
import com.tripcut.domain.user.dto.response.KakaoTokenResponse;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import com.tripcut.domain.user.service.AuthService;
import com.tripcut.domain.user.util.AuthConverter;
import com.tripcut.domain.user.util.KakaoUtil;
import com.tripcut.global.security.jwt.TokenProvider;
import com.tripcut.global.security.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;



    @Override
    public AuthLoginResult kakaoLoginWithOAuthToken(KakaoTokenResponse token) {
        token.setFirst_login(false);
        KakaoDto.KakaoProfile profile = kakaoUtil.requestProfileWithAccessToken(token.getAccess_token());
        String email = profile.getKakao_account().getEmail();
        String nickname = profile.getKakao_account().getProfile().getNickname();
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User u = AuthConverter.toUser(email, nickname, "1234", passwordEncoder);
            token.setFirst_login(true);
            return userRepository.save(u);
        });


        // 4) 우리 서비스 JWT 발급
        String role = String.valueOf(user.getRole());
        var authorities = java.util.List.of(new SimpleGrantedAuthority(role));
        var authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

        TokenDto tokens = tokenProvider.createTokens(authentication);
        return new AuthLoginResult(user, tokens.getAccessToken(), tokens.getRefreshToken(), token.getFirst_login());
    }
}