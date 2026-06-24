package com.web.webhook.repository;

import com.web.webhook.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository
        extends JpaRepository<Campaign, Long> {

    Long countByCreatedBy(String createdBy);
}
