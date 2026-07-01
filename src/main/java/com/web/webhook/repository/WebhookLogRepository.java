package com.web.webhook.repository;

import com.web.webhook.entity.WebhookLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebhookLogRepository
        extends JpaRepository<WebhookLog, Long> {

    // existing method — same as before
    Optional<WebhookLog> findByMessageIdAndStatus(
            String messageId,
            String status
    );

    // NEW: all incoming messages for inbox
    List<WebhookLog> findByStatus(
            String status
    );

    // NEW: conversation with specific phone number
    List<WebhookLog> findByPhoneNumberAndStatus(
            String phoneNumber,
            String status
    );

    // NEW: all messages for a specific phone number
    List<WebhookLog> findByPhoneNumber(
            String phoneNumber
    );
}