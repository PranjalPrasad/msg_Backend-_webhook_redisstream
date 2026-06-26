package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.RescheduleCampaignRequestDto;
import com.web.webhook.dto.requestDto.ScheduleCampaignRequestDto;
import com.web.webhook.dto.responseDto.ScheduleCampaignResponseDto;
import com.web.webhook.service.CampaignSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign-scheduler")
public class CampaignSchedulerController {

    private final CampaignSchedulerService campaignSchedulerService;

    public CampaignSchedulerController(
            CampaignSchedulerService campaignSchedulerService) {

        this.campaignSchedulerService = campaignSchedulerService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleCampaignResponseDto> scheduleCampaign(
            @RequestBody ScheduleCampaignRequestDto requestDto) {

        return ResponseEntity.ok(
                campaignSchedulerService.scheduleCampaign(requestDto)
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ScheduleCampaignResponseDto>> getAllScheduledCampaigns() {

        return ResponseEntity.ok(
                campaignSchedulerService.getAllScheduledCampaigns()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ScheduleCampaignResponseDto> getScheduledCampaign(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                campaignSchedulerService.getScheduledCampaign(id)
        );
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelScheduledCampaign(
            @PathVariable Long id) {

        campaignSchedulerService.cancelScheduledCampaign(id);

        return ResponseEntity.ok("Campaign Cancelled Successfully");
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<String> rescheduleCampaign(
            @PathVariable Long id,
            @RequestBody RescheduleCampaignRequestDto requestDto) {

        campaignSchedulerService.rescheduleCampaign(id, requestDto);

        return ResponseEntity.ok("Campaign Rescheduled Successfully");
    }

}
