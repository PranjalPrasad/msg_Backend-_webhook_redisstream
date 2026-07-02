package com.web.webhook.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    // what action was performed: LOGIN, LOGOUT, CREATE, UPDATE, DELETE, SEND, EXPORT
    private String action;

    // which module: AUTH, CAMPAIGN, CONTACT, TEMPLATE, GROUP, MESSAGE, REPORT, API_KEY
    private String module;

    // extra detail — jaise "Campaign: Festival 2025 created"
    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}