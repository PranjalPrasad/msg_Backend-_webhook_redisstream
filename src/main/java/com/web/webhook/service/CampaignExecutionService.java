package com.web.webhook.service;

import com.web.webhook.dto.responseDto.CampaignExecutionResponseDto;

public interface CampaignExecutionService {

    CampaignExecutionResponseDto executeCampaign(
            Long campaignId
    );
}