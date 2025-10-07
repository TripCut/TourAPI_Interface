package com.tripcut.domain.user.service;

import com.tripcut.domain.user.dto.MemberDto;
import com.tripcut.domain.user.dto.request.MemberUpdateRequest;
import com.tripcut.domain.user.entity.User;

public interface MemberService {

    MemberDto signup(MemberDto memberDto);

    MemberDto getUserWithAuthorities(String memberId);

    MemberDto getMyUserWithAuthorities();

    User updateMemberInfo(MemberUpdateRequest memberUpdateRequest);

    MemberDto signupWithEmailCode(MemberDto memberDto, String verificationCode);
}
