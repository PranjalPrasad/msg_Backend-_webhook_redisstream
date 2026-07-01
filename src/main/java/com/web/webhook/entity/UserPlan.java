package com.web.webhook.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_plans")
public class UserPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String createdBy;

    // plan name — Premium, Basic, Free
    private String planName;

    // monthly price
    private Double price;

    // total messages allowed per month
    private Integer messageLimit;

    // messages used this month
    private Integer messagesUsed;

    // current credit balance
    private Double creditBalance;

    // plan valid till date
    private LocalDateTime validTill;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

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

    public LocalDateTime getValidTill() { return validTill; }
    public void setValidTill(LocalDateTime validTill) { this.validTill = validTill; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

