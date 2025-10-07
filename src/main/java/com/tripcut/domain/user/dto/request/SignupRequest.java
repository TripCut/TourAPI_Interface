package com.tripcut.domain.user.dto.request;

import com.tripcut.domain.user.dto.MemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @Valid
    private MemberDto member;
    @NotBlank
    private String verificationCode;
}
