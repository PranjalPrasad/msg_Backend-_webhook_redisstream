package com.web.webhook.dto.responseDto;

public class UserPlanResponseDto {

    private Long id;
    private String planName;
    private Double price;
    private Integer messageLimit;
    private Integer messagesUsed;
    private Double creditBalance;
    private String validTill;
    private String createdBy;
    private String message;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getMessageLimit() { return messageLimit; }
    public void setMessageLimit(Integer messageLimit) { this.messageLimit = messageLimit; }

    public Integer getMessagesUsed() { return messagesUsed; }
    public void setMessagesUsed(Integer messagesUsed) { this.messagesUsed = messagesUsed; }

    public Double getCreditBalance() { return creditBalance; }
    public void setCreditBalance(Double creditBalance) { this.creditBalance = creditBalance; }

    public String getValidTill() { return validTill; }
    public void setValidTill(String validTill) { this.validTill = validTill; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}


