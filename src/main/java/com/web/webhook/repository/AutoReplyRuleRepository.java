package com.web.webhook.repository;

import com.web.webhook.entity.AutoReplyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoReplyRuleRepository
        extends JpaRepository<AutoReplyRule, Long> {

    List<AutoReplyRule> findByCreatedBy(
            String createdBy
    );

    Optional<AutoReplyRule> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    List<AutoReplyRule> findByStatus(
            String status
    );
}
