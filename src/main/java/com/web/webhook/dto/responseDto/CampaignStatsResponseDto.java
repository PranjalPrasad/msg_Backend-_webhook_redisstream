package com.web.webhook.dto.responseDto;

public class CampaignStatsResponseDto {

    private Long campaignId;

    private String campaignName;

    private Integer totalContacts;

    private Integer sentCount;

    private Integer deliveredCount;

    private Integer failedCount;

    private String status;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Integer getTotalContacts() {
        return totalContacts;
    }

    public void setTotalContacts(Integer totalContacts) {
        this.totalContacts = totalContacts;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getDeliveredCount() {
        return deliveredCount;
    }

    public void setDeliveredCount(Integer deliveredCount) {
        this.deliveredCount = deliveredCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
