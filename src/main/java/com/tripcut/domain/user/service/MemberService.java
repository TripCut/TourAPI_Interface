package com.tripcut.domain.user.service;

import com.tripcut.domain.user.dto.MemberDto;
import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.repository.UserRepository;
import com.tripcut.global.common.exception.DuplicateMemberException;
import com.tripcut.global.common.exception.NotFoundMemberException;
import com.tripcut.global.security.jwt.SecurityUtil;
import com.tripcut.global.security.jwt.entity.Authority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class MemberService {
    private final UserRepository memberRepository;

//    private final MemberMapper memberMapper;

    private final PasswordEncoder passwordEncoder;

    //    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
//        this.memberRepository = memberRepository;
//        this.memberMapper = memberMapper;
//        this.passwordEncoder = passwordEncoder;
//    }
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

        return MemberDto.from(memberRepository.save(user));
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

//    public MemberDto getMemberById(String memberNo) {
//        // 파라미터를 HashMap으로 준비
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("memberNo", memberNo);
//        return memberMapper.findByMemberId(parameters);
//    }
    public MemberDto getMyInfo(Long id){
        MemberDto memberDto = UserRepository.findById(id);
        return memberDto;
    }

}