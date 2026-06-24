package com.web.webhook.dto.responseDto;

public class CampaignAnalyticsResponseDTO {

    private Long campaignId;
    private Long totalMessages;
    private Long sentCount;
    private Long deliveredCount;
    private Long readCount;
    private Long failedCount;

    private Double deliveryRate;
    private Double readRate;

    public CampaignAnalyticsResponseDTO() {
    }

    public CampaignAnalyticsResponseDTO(
            Long campaignId,
            Long totalMessages,
            Long sentCount,
            Long deliveredCount,
            Long readCount,
            Long failedCount,
            Double deliveryRate,
            Double readRate) {

        this.campaignId = campaignId;
        this.totalMessages = totalMessages;
        this.sentCount = sentCount;
        this.deliveredCount = deliveredCount;
        this.readCount = readCount;
        this.failedCount = failedCount;
        this.deliveryRate = deliveryRate;
        this.readRate = readRate;
    }

    // getters setters


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(Long totalMessages) {
        this.totalMessages = totalMessages;
    }

    public Long getSentCount() {
        return sentCount;
    }

    public void setSentCount(Long sentCount) {
        this.sentCount = sentCount;
    }

    public Long getDeliveredCount() {
        return deliveredCount;
    }

    public void setDeliveredCount(Long deliveredCount) {
        this.deliveredCount = deliveredCount;
    }

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Double getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(Double deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    public Double getReadRate() {
        return readRate;
    }

    public void setReadRate(Double readRate) {
        this.readRate = readRate;
    }
}
