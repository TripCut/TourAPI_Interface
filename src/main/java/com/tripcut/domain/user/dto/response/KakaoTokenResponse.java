package com.tripcut.domain.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenResponse {
    public String access_token;
    public String refresh_token;
    public Long  expires_in;
    public Long  refresh_token_expires_in;
    public String scope;
    public String token_type;
    public Boolean first_login;
}
