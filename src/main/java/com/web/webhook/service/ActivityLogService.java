package com.web.webhook.service;

import com.web.webhook.dto.responseDto.ActivityLogResponseDto;

import java.util.List;

public interface ActivityLogService {

    // log save karo
    void log(String userEmail, String action, String module, String description);

    // saare logs
    List<ActivityLogResponseDto> getAllLogs();

    // specific user ke logs
    List<ActivityLogResponseDto> getLogsByUser(String userEmail);

    // specific module ke logs
    List<ActivityLogResponseDto> getLogsByModule(String module);

    // specific action ke logs
    List<ActivityLogResponseDto> getLogsByAction(String action);
}