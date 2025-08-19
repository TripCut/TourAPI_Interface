package com.tripcut.domain.user.dto.response;

import lombok.Getter;

@Getter
public class KakaoTokenResponse {
    public String access_token;
    public String refresh_token;
    public Long   expires_in;
    public Long   refresh_token_expires_in;
    public String scope;
    public String token_type;
}
