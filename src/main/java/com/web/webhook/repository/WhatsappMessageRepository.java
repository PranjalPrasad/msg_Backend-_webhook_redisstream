package com.web.webhook.repository;

import com.web.webhook.entity.WhatsappMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WhatsappMessageRepository
        extends JpaRepository<WhatsappMessage, Long> {

    // existing methods — same as before
    List<WhatsappMessage> findByCreatedBy(
            String createdBy
    );

    Optional<WhatsappMessage> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    long countByStatusAndCreatedBy(
            String status,
            String createdBy
    );

    long countByCreatedBy(
            String createdBy
    );

    // us wamid se message dhundhna hoga campaign counter update ke liye
    Optional<WhatsappMessage> findByMetaMessageId(
            String metaMessageId
    );

    // NEW: campaign ke kitne messages sent/delivered/failed hue
    long countByCampaignIdAndStatus(
            Long campaignId,
            String status
    );

    // NEW: campaign ke total messages
    long countByCampaignId(
            Long campaignId
    );
}
