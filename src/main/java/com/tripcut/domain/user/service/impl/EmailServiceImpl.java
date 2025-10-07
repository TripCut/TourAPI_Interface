package com.tripcut.domain.user.service.impl;

import com.tripcut.domain.user.entity.EmailMessage;
import com.tripcut.domain.user.repository.EmailMessageRepository;
import com.tripcut.domain.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailMessageRepository emailMessageRepository;
    private final SpringTemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;

    public static final String KEY_PREFIX="authCode:";
    public static final String VERIFIED_PREFIX = "verified:";
    public static final Duration AUTH_TTL = Duration.ofMinutes(3);

    public static final Duration VERIFIED_TTL = Duration.ofSeconds(185);


    public String sendVerificationEmail(String emailRecipient) {
        String authCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        saveAuthCode(emailRecipient, authCode);

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
    private String buildVerifiedKey(String email) {
        return VERIFIED_PREFIX + email.trim().toLowerCase();
    }

    private void saveAuthCode(String email, String code) {
        String key = buildKey(email);
        redisTemplate.opsForValue().set(key, code, AUTH_TTL);
    }
    private String buildKey(String email){
        return KEY_PREFIX+email.trim().toLowerCase();
    }

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

    public boolean isEmailVerified(String email) {
        return redisTemplate.opsForValue().get(buildVerifiedKey(email)) != null;
    }

    public void clearEmailVerified(String email) {
        redisTemplate.delete(buildVerifiedKey(email));
    }

    public String buildEmailContent(String authCode) {
        Context context = new Context();
        context.setVariable("authCode", authCode);
        System.out.println("authCode :"+ authCode);

        return templateEngine.process("email", context);
    }


    public boolean verifyCode(String email, String code) {
        String key = buildKey(email);
        String saved = redisTemplate.opsForValue().get(key);
        boolean matched = saved != null && saved.equals(code);
        if (matched){
            redisTemplate.delete(key);
            if (VERIFIED_TTL != null) {
                redisTemplate.opsForValue().set(buildVerifiedKey(email), "1", VERIFIED_TTL);
            } else {
                redisTemplate.opsForValue().set(buildVerifiedKey(email), "1");
            }
        }
        return matched;
    }
}

