package com.tripcut.domain.user.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripcut.domain.user.dto.KakaoDto;
import com.tripcut.domain.user.exception.handler.AuthHandler;
import com.tripcut.domain.user.exception.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
@Slf4j
public class KakaoUtil {

    @Value("${spring.kakao.auth.client}")
    private String client;
    @Value("${spring.kakao.auth.redirect}")
    private String redirect;

    public KakaoDto.OAuthToken requestToken(String accessCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirect);
        params.add("code", accessCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        KakaoDto.OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), KakaoDto.OAuthToken.class);
            log.info("oAuthToken : " + oAuthToken.getAccess_token());
        } catch (JsonProcessingException e) {
            throw new AuthHandler(ErrorStatus._PARSING_ERROR);
        }
        return oAuthToken;
    }

    public KakaoDto.KakaoProfile requestProfileWithAccessToken(String accessToken){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> req = new HttpEntity<>(headers);
        ResponseEntity<String> res = rest.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                req,
                String.class
        );
        try {
            return new ObjectMapper().readValue(res.getBody(), KakaoDto.KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new AuthHandler(ErrorStatus._PARSING_ERROR);
        }
    }
}
