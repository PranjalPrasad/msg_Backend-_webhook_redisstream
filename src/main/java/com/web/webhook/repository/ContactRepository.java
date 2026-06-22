package com.web.webhook.repository;

import com.web.webhook.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository
        extends JpaRepository<Contact,Long> {

    List<Contact> findByCreatedBy(
            String createdBy
    );

    Optional<Contact> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    boolean existsByPhoneNumberAndCreatedBy(
            String phoneNumber,
            String createdBy
    );

    List<Contact> findByIdIn(
            List<Long> ids
    );

}