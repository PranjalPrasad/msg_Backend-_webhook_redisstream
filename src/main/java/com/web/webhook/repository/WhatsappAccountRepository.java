package com.web.webhook.repository;

import com.web.webhook.entity.WhatsappAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WhatsappAccountRepository
        extends JpaRepository<WhatsappAccount, Long> {

    List<WhatsappAccount> findByCreatedBy(
            String createdBy
    );

    Optional<WhatsappAccount> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    boolean existsByPhoneNumberIdAndCreatedBy(
            String phoneNumberId,
            String createdBy
    );
}