package com.tripcut.domain.user.service.impl;

import com.tripcut.domain.user.entity.EmailMessage;
import com.tripcut.domain.user.repository.EmailMessageRepository;
import com.tripcut.domain.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailMessageRepository emailMessageRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

    /**
     * 이메일 인증 요청 (이메일 저장 + 전송)
     */
    public String sendVerificationEmail(String emailRecipient) {
        String authCode = UUID.randomUUID().toString().substring(0, 6); // 6자리 인증번호 생성

        // 이메일 정보 저장
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setEmailRecipient(emailRecipient);
        emailMessage.setEmailTitle("TripCut 이메일 인증");
        emailMessage.setEmailContent("인증번호: " + authCode);
        emailMessage.setAuthCode(authCode);
        emailMessageRepository.save(emailMessage);

        // 이메일 전송
        try {
            sendEmail(emailRecipient, "TripCut 이메일 인증", buildEmailContent(authCode));
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }

        return authCode;
    }

    /**
     * 이메일 전송 (HTML 지원)
     */

    private void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        try {
            helper.setFrom(new InternetAddress("alstjr6823@naver.com", "Tripcut", StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Failed to set From address", e);
        }

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }


    public String buildEmailContent(String authCode) {
        Context context = new Context();
        context.setVariable("authCode", authCode);

        return templateEngine.process("email", context);
    }

    /**
     * 인증 코드 검증
     */
    public boolean verifyCode(String email, String code) {
        Optional<EmailMessage> emailMessageOpt = emailMessageRepository.findByEmailRecipientAndAuthCode(email, code);
        return emailMessageOpt.isPresent();
    }
}

