package com.tripcut.domain.user.controller;

import com.tripcut.core.controller.BaseController;
import com.tripcut.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tripcut.global.common.api.ApiPath.BASE_URL;

@RestController
@RequestMapping(BASE_URL+"/email")
@RequiredArgsConstructor
public class EmailController extends BaseController {
    private final EmailService emailService;

    /**
     * 이메일 인증 요청 API
     */
    @PostMapping("/request-verification")
    public ResponseEntity<String> requestVerification(@RequestParam String email) {
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 이메일이 전송되었습니다.");
    }

    /**
     * 이메일 인증 확인 API
     */
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String code) {
        boolean isValid = emailService.verifyCode(email, code);

        if (!isValid) {
            return ResponseEntity.badRequest().body("유효하지 않은 인증 코드입니다.");
        }

        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }
}