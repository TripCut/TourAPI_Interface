package com.tripcut.domain.user.service.impl;

import com.tripcut.domain.user.dto.MemberDto;
import com.tripcut.domain.user.dto.request.MemberUpdateRequest;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import com.tripcut.domain.user.service.EmailService;
import com.tripcut.domain.user.service.MemberService;
import com.tripcut.global.common.exception.DuplicateMemberException;
import com.tripcut.global.common.exception.NotFoundMemberException;
import com.tripcut.global.security.jwt.SecurityUtil;
import com.tripcut.global.security.jwt.entity.Authority;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class MemberServiceImpl implements MemberService {
    private final UserRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    public MemberServiceImpl(UserRepository memberRepository, PasswordEncoder passwordEncoder, EmailServiceImpl emailService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public MemberDto signup(MemberDto memberDto) {

        String email = memberDto.getMemberId();

        System.out.println(emailService.isEmailVerified(email));
        if (email == null || email.isBlank() || !emailService.isEmailVerified(email)) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        if (memberRepository.findOneWithAuthoritiesByMemberId(memberDto.getMemberId()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        User user = User.builder()
                .memberId(memberDto.getMemberId())
                .memberPw(passwordEncoder.encode(memberDto.getMemberPw()))
                .name(memberDto.getName())
                .email(memberDto.getMemberId())
                .role(memberDto.getRole())
                .preferredLanguage(memberDto.getPreferredLanguage())
                .preferredGenres(memberDto.getPreferredGenres())
                .badges(memberDto.getBadges())
                .build();


        emailService.clearEmailVerified(email);
        return MemberDto.from(memberRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String memberId) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByMemberId(memberId).orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentMemberId()
                        .flatMap(memberRepository::findOneWithAuthoritiesByMemberId)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    @Override
    @Transactional
    public User updateMemberInfo(MemberUpdateRequest memberUpdateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = memberRepository.findIdByMemberId(authentication.getName());
        User user = memberRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("해당 회원을 찾을 수 없습니다. id=" + id));
        user.setMemberPw(passwordEncoder.encode(memberUpdateRequest.getMemberPw()));
        user.setName(memberUpdateRequest.getName());
        user.setPreferredLanguage(memberUpdateRequest.getPreferredLanguage());
        user.setPreferredGenres(memberUpdateRequest.getPreferredGenres());
        user.setBadges(memberUpdateRequest.getBadges());
        return memberRepository.save(user);
    }

    @Override
    @Transactional
    public MemberDto signupWithEmailCode(MemberDto memberDto, String verificationCode) {
        String email = memberDto.getEmail();
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("이메일을 입력해 주세요.");
        }
        if(verificationCode == null || verificationCode.isBlank()){
            throw new IllegalArgumentException("인증번호를 입력해 주세요.");
        }
        boolean verified = emailService.verifyCode(email.trim(), verificationCode.trim());
        if(!verified){
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }
        User user = User.builder()
                .memberId(memberDto.getMemberId())
                .memberPw(passwordEncoder.encode(memberDto.getMemberPw()))
                .name(memberDto.getName())
                .email(email.trim())
                .role(memberDto.getRole())
                .preferredLanguage(memberDto.getPreferredLanguage())
                .preferredGenres(memberDto.getPreferredGenres())
                .badges(memberDto.getBadges())
                .build();
        try {
            return MemberDto.from(memberRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            // email/memberId 유니크 충돌 등
            throw new DuplicateMemberException("이미 가입되어 있는 계정 정보입니다.");
        }
    }
}