package com.web.webhook.service;

import com.web.webhook.dto.requestDto.RescheduleCampaignRequestDto;
import com.web.webhook.dto.requestDto.ScheduleCampaignRequestDto;
import com.web.webhook.dto.responseDto.ScheduleCampaignResponseDto;

import java.util.List;

public interface CampaignSchedulerService {

    ScheduleCampaignResponseDto scheduleCampaign(
            ScheduleCampaignRequestDto requestDto
    );

    List<ScheduleCampaignResponseDto> getAllScheduledCampaigns();

    ScheduleCampaignResponseDto getScheduledCampaign(
            Long id
    );

    void cancelScheduledCampaign(
            Long id
    );
    void rescheduleCampaign(
            Long id,
            RescheduleCampaignRequestDto requestDto);
}
