package com.tripcut.domain.user.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.user.dto.response.*;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.service.AuthService;
import com.tripcut.domain.user.util.UserConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoAuthController extends BaseController {

    private final AuthService authService;


    @GetMapping("/api/auth/login/kakao")
    public String redirectToKakao() {
        return "redirect:" + "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=baf916b3a07072b8333ad4dd2c9481cc"
                + "&redirect_uri=http://localhost:8080/auth/login/kakao"
                + "&response_type=code";
    }


    @PostMapping("/auth/login/kakao")
    public BaseResponse<LoginResponse> kakaoLogin(
            @RequestParam("code") String authorizeCode,
            HttpServletResponse httpServletResponse
    ) {
        // 1. 요청 파라미터 만들기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "baf916b3a07072b8333ad4dd2c9481cc"); // REST API KEY
        params.add("redirect_uri", "http://localhost:8080/auth/login/kakao");
        params.add("code", authorizeCode);

        // 2. HTTP 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 3. 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                new HttpEntity<>(params, headers),
                KakaoTokenResponse.class
        );
        KakaoTokenResponse token = response.getBody();

        AuthLoginResult result = authService.kakaoLoginWithOAuthToken(token);

        LoginResponse body = new LoginResponse(
                UserConverter.toJoinResultDTO(result.getUser()),
                result.getAccessToken(),
                result.getRefreshToken(),
                result.getFirstLogin()
        );
        System.out.println(body);
        return BaseResponse.onSuccess(body);
    }

}
