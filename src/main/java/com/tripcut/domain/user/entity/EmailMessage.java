package com.tripcut.domain.user.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@RequiredArgsConstructor
public class EmailMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMAIL_MESSAGE_ID")
    private Long emailMessageId;

    @Column(name = "EMAIL_RECIPIENT", nullable = false)
    private String emailRecipient;

    @Column(name = "EMAIL_TITLE")
    private String emailTitle;

    @Column(name = "EMAIL_CONTENT", columnDefinition = "TEXT")
    private String emailContent;

    @Column(name = "SENT_AT")
    private String sentAt;

    @Column(name = "AUTH_CODE", nullable = false)
    private String authCode;

    @Column(name = "VERIFIED")
    private boolean verified = false;


    public void verify() {
        this.verified = true;
    }

    private String getCurrentTime() {
        ZonedDateTime nowKst = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return nowKst.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void setEmailRecipent(String recipientEmail) {
    }
}