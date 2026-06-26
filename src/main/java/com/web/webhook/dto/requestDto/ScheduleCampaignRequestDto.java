package com.web.webhook.dto.requestDto;


import java.time.LocalDateTime;

public class ScheduleCampaignRequestDto {

    private Long campaignId;

    private LocalDateTime scheduledTime;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
