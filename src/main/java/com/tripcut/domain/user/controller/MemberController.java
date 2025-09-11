package com.tripcut.domain.user.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.user.dto.LoginDto;
import com.tripcut.domain.user.dto.MemberDto;
import com.tripcut.domain.user.repository.UserRepository;
import com.tripcut.domain.user.service.MemberService;
import com.tripcut.global.security.jwt.TokenProvider;
import com.tripcut.global.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;


@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL + "/member")
public class MemberController extends BaseController {
    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    /** 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@Valid @RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(memberService.signup(memberDto));
    }

    /** 회원 전용 회원 리스트 조회 */
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/list")
    public ResponseEntity<MemberDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    /** 관리자 전용 회원 조회 */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMemberInfo(@PathVariable String memberId) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(memberId));
    }

    /** 로그인 (AccessToken + RefreshToken 발급) */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getMemberId(), loginDto.getMemberPw());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // AccessToken + RefreshToken 발급
        TokenDto tokenDto = tokenProvider.createTokens(authentication);

        // HTTP 헤더에 AccessToken 포함
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken());

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = tokenProvider.regenerateAccessToken(refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
    @GetMapping("/info/{memberId}")
    public MemberDto readMyInfo(@RequestParam Long memberId){
        return userRepository.findById(memberId)
                .map(MemberDto::from)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

}