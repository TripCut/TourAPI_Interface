package com.tripcut.domain.user.service;

import com.tripcut.domain.user.dto.MemberDto;
import com.tripcut.domain.user.dto.request.MemberUpdateRequest;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
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
public class MemberService {
    private final UserRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(UserRepository memberRepository,  PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findOneWithAuthoritiesByMemberId(memberDto.getMemberId()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .memberId(memberDto.getMemberId())
                .memberPw(passwordEncoder.encode(memberDto.getMemberPw()))
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .role(memberDto.getRole())
                .preferredLanguage(memberDto.getPreferredLanguage())
                .preferredGenres(memberDto.getPreferredGenres())
                .badges(memberDto.getBadges())
                .build();

        try {
            return MemberDto.from(memberRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            // email/ memberId 유니크 충돌 등
            throw new DuplicateMemberException("이미 가입되어 있는 계정 정보입니다.");
        }
    }

    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String memberId) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByMemberId(memberId).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
                SecurityUtil.getCurrentMemberId()
                        .flatMap(memberRepository::findOneWithAuthoritiesByMemberId)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    @Transactional
    public User updateMemberInfo(MemberUpdateRequest memberUpdateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //System.out.println("name: "+authentication.getName()); => memberId
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


}