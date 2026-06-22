package com.web.webhook.repository;
import com.web.webhook.entity.ContactGroupMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactGroupMappingRepository
        extends JpaRepository<ContactGroupMapping, Long> {

    List<ContactGroupMapping> findByGroupId(
            Long groupId
    );

    boolean existsByContactIdAndGroupId(
            Long contactId,
            Long groupId
    );
}