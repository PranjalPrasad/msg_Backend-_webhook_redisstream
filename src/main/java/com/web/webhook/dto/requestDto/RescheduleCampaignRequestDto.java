package com.web.webhook.dto.requestDto;

import java.time.LocalDateTime;

public class RescheduleCampaignRequestDto {

    private LocalDateTime scheduledTime;

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
