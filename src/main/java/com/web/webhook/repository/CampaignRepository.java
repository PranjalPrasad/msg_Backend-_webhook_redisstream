package com.web.webhook.repository;

import com.web.webhook.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository
        extends JpaRepository<Campaign, Long> {

    List<Campaign> findByCreatedBy(
            String createdBy
    );

    Optional<Campaign> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    boolean existsByCampaignNameAndCreatedBy(
            String campaignName,
            String createdBy
    );

    Long countByCreatedBy(
            String createdBy
    );
}