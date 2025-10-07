package com.tripcut.domain.user.dto.request;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String code;
}
