package com.web.webhook.repository;

import com.web.webhook.entity.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageTemplateRepository
        extends JpaRepository<MessageTemplate, Long> {

    List<MessageTemplate> findByCreatedBy(
            String createdBy
    );

    Optional<MessageTemplate> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    boolean existsByTemplateNameAndCreatedBy(
            String templateName,
            String createdBy
    );
}