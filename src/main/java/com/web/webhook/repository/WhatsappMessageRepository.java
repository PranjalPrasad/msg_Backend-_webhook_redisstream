package com.web.webhook.repository;

import com.web.webhook.entity.WhatsappMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WhatsappMessageRepository
        extends JpaRepository<WhatsappMessage, Long> {

    List<WhatsappMessage> findByCreatedBy(String createdBy);

    Optional<WhatsappMessage> findByIdAndCreatedBy(Long id, String createdBy);

    long countByStatusAndCreatedBy(String status, String createdBy);

    long countByCreatedBy(String createdBy);

    Optional<WhatsappMessage> findByMetaMessageId(String metaMessageId);

    long countByCampaignIdAndStatus(Long campaignId, String status);

    long countByCampaignId(Long campaignId);
}