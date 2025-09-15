package com.tripcut.domain.user.repository;


import com.tripcut.domain.user.entity.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
    Optional<EmailMessage> findByEmailRecipientAndAuthCode(String emailRecipient, String authCode);
}