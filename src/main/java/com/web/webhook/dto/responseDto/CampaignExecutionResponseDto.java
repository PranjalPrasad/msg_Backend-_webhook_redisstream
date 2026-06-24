package com.web.webhook.dto.responseDto;

public class CampaignExecutionResponseDto {

    private Long campaignId;

    private Integer totalContacts;

    private Integer messagesQueued;

    private String status;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getTotalContacts() {
        return totalContacts;
    }

    public void setTotalContacts(Integer totalContacts) {
        this.totalContacts = totalContacts;
    }

    public Integer getMessagesQueued() {
        return messagesQueued;
    }

    public void setMessagesQueued(Integer messagesQueued) {
        this.messagesQueued = messagesQueued;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}