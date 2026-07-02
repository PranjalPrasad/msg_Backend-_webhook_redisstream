package com.web.webhook.controller;

import com.web.webhook.dto.responseDto.ActivityLogResponseDto;
import com.web.webhook.service.ActivityLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity-logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(
            ActivityLogService activityLogService) {

        this.activityLogService = activityLogService;
    }

    // saare logs
    @GetMapping
    public ResponseEntity<List<ActivityLogResponseDto>>
    getAllLogs() {

        return ResponseEntity.ok(
                activityLogService.getAllLogs()
        );
    }

    // specific user ke logs
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<ActivityLogResponseDto>>
    getLogsByUser(
            @PathVariable String userEmail) {

        return ResponseEntity.ok(
                activityLogService.getLogsByUser(userEmail)
        );
    }

    // specific module ke logs
    @GetMapping("/module/{module}")
    public ResponseEntity<List<ActivityLogResponseDto>>
    getLogsByModule(
            @PathVariable String module) {

        return ResponseEntity.ok(
                activityLogService.getLogsByModule(module)
        );
    }

    // specific action ke logs
    @GetMapping("/action/{action}")
    public ResponseEntity<List<ActivityLogResponseDto>>
    getLogsByAction(
            @PathVariable String action) {

        return ResponseEntity.ok(
                activityLogService.getLogsByAction(action)
        );
    }

    // manual log create karo (testing ke liye)
    @PostMapping
    public ResponseEntity<String>
    createLog(
            @RequestParam String userEmail,
            @RequestParam String action,
            @RequestParam String module,
            @RequestParam String description) {

        activityLogService.log(
                userEmail, action, module, description
        );

        return ResponseEntity.ok(
                "Activity log created successfully"
        );
    }
}