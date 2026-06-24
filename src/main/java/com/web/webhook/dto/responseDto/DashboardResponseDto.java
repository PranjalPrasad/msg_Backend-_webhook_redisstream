package com.web.webhook.dto.responseDto;

public class DashboardResponseDto {

    private Long totalCampaigns;

    private Long totalMessages;

    private Long totalDelivered;

    private Long totalFailed;

    public Long getTotalCampaigns() {
        return totalCampaigns;
    }

    public void setTotalCampaigns(Long totalCampaigns) {
        this.totalCampaigns = totalCampaigns;
    }

    public Long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Long totalMessages) {
        this.totalMessages = totalMessages;
    }

    public Long getTotalDelivered() {
        return totalDelivered;
    }

    public void setTotalDelivered(Long totalDelivered) {
        this.totalDelivered = totalDelivered;
    }

    public Long getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(Long totalFailed) {
        this.totalFailed = totalFailed;
    }
}