package com.web.webhook.repository;

import com.web.webhook.entity.ScheduledCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledCampaignRepository
        extends JpaRepository<ScheduledCampaign, Long> {

    List<ScheduledCampaign> findByCreatedBy(
            String createdBy
    );

    ScheduledCampaign findByCampaignId(
            Long campaignId
    );
}
