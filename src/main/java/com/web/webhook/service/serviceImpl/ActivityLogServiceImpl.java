package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.responseDto.ActivityLogResponseDto;
import com.web.webhook.entity.ActivityLog;
import com.web.webhook.repository.ActivityLogRepository;
import com.web.webhook.service.ActivityLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityLogServiceImpl
        implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogServiceImpl(
            ActivityLogRepository activityLogRepository) {

        this.activityLogRepository = activityLogRepository;
    }

    private ActivityLogResponseDto convertToDto(
            ActivityLog log) {

        ActivityLogResponseDto dto =
                new ActivityLogResponseDto();

        dto.setId(log.getId());
        dto.setUserEmail(log.getUserEmail());
        dto.setAction(log.getAction());
        dto.setModule(log.getModule());
        dto.setDescription(log.getDescription());
        dto.setCreatedAt(
                log.getCreatedAt() != null
                        ? log.getCreatedAt().toString() : null
        );

        return dto;
    }

    @Override
    public void log(String userEmail, String action,
                    String module, String description) {

        try {

            ActivityLog log = new ActivityLog();
            log.setUserEmail(userEmail);
            log.setAction(action);
            log.setModule(module);
            log.setDescription(description);
            log.setCreatedAt(LocalDateTime.now());

            activityLogRepository.save(log);

            System.out.println("[ACTIVITY LOG] " + userEmail
                    + " | " + action
                    + " | " + module
                    + " | " + description);

        } catch (Exception e) {
            System.err.println("[ACTIVITY LOG] Failed to save log: "
                    + e.getMessage());
        }
    }

    @Override
    public List<ActivityLogResponseDto> getAllLogs() {

        List<ActivityLog> logs =
                activityLogRepository
                        .findAllByOrderByCreatedAtDesc();

        List<ActivityLogResponseDto> result = new ArrayList<>();

        for (ActivityLog log : logs) {
            result.add(convertToDto(log));
        }

        return result;
    }

    @Override
    public List<ActivityLogResponseDto> getLogsByUser(
            String userEmail) {

        List<ActivityLog> logs =
                activityLogRepository
                        .findByUserEmailOrderByCreatedAtDesc(
                                userEmail
                        );

        List<ActivityLogResponseDto> result = new ArrayList<>();

        for (ActivityLog log : logs) {
            result.add(convertToDto(log));
        }

        return result;
    }

    @Override
    public List<ActivityLogResponseDto> getLogsByModule(
            String module) {

        List<ActivityLog> logs =
                activityLogRepository
                        .findByModuleOrderByCreatedAtDesc(
                                module
                        );

        List<ActivityLogResponseDto> result = new ArrayList<>();

        for (ActivityLog log : logs) {
            result.add(convertToDto(log));
        }

        return result;
    }

    @Override
    public List<ActivityLogResponseDto> getLogsByAction(
            String action) {

        List<ActivityLog> logs =
                activityLogRepository
                        .findByActionOrderByCreatedAtDesc(
                                action
                        );

        List<ActivityLogResponseDto> result = new ArrayList<>();

        for (ActivityLog log : logs) {
            result.add(convertToDto(log));
        }

        return result;
    }
}