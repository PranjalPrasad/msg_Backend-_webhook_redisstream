package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.responseDto.CampaignStatsResponseDto;
import com.web.webhook.dto.responseDto.DashboardResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.CampaignAnalyticsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CampaignAnalyticsServiceImpl
        implements CampaignAnalyticsService {

    private final CampaignRepository campaignRepository;

    private final WhatsappMessageRepository messageRepository;

    public CampaignAnalyticsServiceImpl(
            CampaignRepository campaignRepository,
            WhatsappMessageRepository messageRepository) {

        this.campaignRepository = campaignRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public CampaignStatsResponseDto getCampaignStats(
            Long campaignId) {

        Campaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Campaign Not Found"
                                )
                        );

        CampaignStatsResponseDto dto =
                new CampaignStatsResponseDto();

        dto.setCampaignId(
                campaign.getId()
        );

        dto.setCampaignName(
                campaign.getCampaignName()
        );

        dto.setTotalContacts(
                campaign.getTotalContacts()
        );

        dto.setSentCount(
                campaign.getSentCount()
        );

        dto.setDeliveredCount(
                campaign.getDeliveredCount()
        );

        dto.setFailedCount(
                campaign.getFailedCount()
        );

        dto.setStatus(
                campaign.getStatus()
        );

        return dto;
    }

    @Override
    public DashboardResponseDto getDashboardSummary() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        DashboardResponseDto dto =
                new DashboardResponseDto();

        dto.setTotalCampaigns(
                campaignRepository
                        .countByCreatedBy(email)
        );

        dto.setTotalMessages(
                messageRepository
                        .countByCreatedBy(email)
        );

        dto.setTotalDelivered(
                messageRepository
                        .countByStatusAndCreatedBy(
                                "DELIVERED",
                                email
                        )
        );

        dto.setTotalFailed(
                messageRepository
                        .countByStatusAndCreatedBy(
                                "FAILED",
                                email
                        )
        );

        return dto;
    }
}
