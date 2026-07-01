
package com.web.webhook.dto.requestDto;

public class UpdatePlanRequestDto {

    private String planName;

    private Double price;

    private Integer messageLimit;

    private String validTill;

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getMessageLimit() { return messageLimit; }
    public void setMessageLimit(Integer messageLimit) { this.messageLimit = messageLimit; }

    public String getValidTill() { return validTill; }
    public void setValidTill(String validTill) { this.validTill = validTill; }
}
