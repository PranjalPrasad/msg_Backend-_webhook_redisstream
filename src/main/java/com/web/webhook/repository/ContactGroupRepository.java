package com.web.webhook.repository;

import com.web.webhook.entity.Contact;
import com.web.webhook.entity.ContactGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactGroupRepository
        extends JpaRepository<ContactGroup, Long> {

    List<ContactGroup> findByCreatedBy(
            String createdBy
    );

    Optional<ContactGroup> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    boolean existsByGroupNameAndCreatedBy(
            String groupName,
            String createdBy
    );


}