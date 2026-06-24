package com.web.webhook.controller;

import com.web.webhook.dto.responseDto.CampaignStatsResponseDto;
import com.web.webhook.dto.responseDto.DashboardResponseDto;
import com.web.webhook.service.CampaignAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaign-analytics")
public class CampaignAnalyticsController {

    private final CampaignAnalyticsService service;

    public CampaignAnalyticsController(
            CampaignAnalyticsService service) {

        this.service = service;
    }

    @GetMapping("/campaign-stats/{campaignId}")
    public ResponseEntity<CampaignStatsResponseDto>
    getCampaignStats(
            @PathVariable Long campaignId){

        return ResponseEntity.ok(
                service.getCampaignStats(
                        campaignId
                )
        );
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<DashboardResponseDto>
    getDashboardSummary(){

        return ResponseEntity.ok(
                service.getDashboardSummary()
        );
    }
}
