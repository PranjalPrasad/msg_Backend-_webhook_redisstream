package com.web.webhook.dto.responseDto;

public class WhatsappAccountResponseDto {

    private Long id;

    private String businessAccountId;

    private String phoneNumberId;

    private String verifyToken;

    private String webhookUrl;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessAccountId() {
        return businessAccountId;
    }

    public void setBusinessAccountId(String businessAccountId) {
        this.businessAccountId = businessAccountId;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
