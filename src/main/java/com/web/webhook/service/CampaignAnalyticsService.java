package com.web.webhook.service;

import com.web.webhook.dto.responseDto.CampaignStatsResponseDto;
import com.web.webhook.dto.responseDto.DashboardResponseDto;

public interface CampaignAnalyticsService {

    CampaignStatsResponseDto getCampaignStats(
            Long campaignId
    );

    DashboardResponseDto getDashboardSummary();


}

