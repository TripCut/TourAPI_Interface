package com.tripcut.domain.user.service;

public interface EmailService {
    String sendVerificationEmail(String emailRecipient);

    String buildEmailContent(String authCode);

    boolean verifyCode(String email, String code);
}
