package com.web.webhook.controller;

import com.web.webhook.dto.responseDto.CampaignExecutionResponseDto;
import com.web.webhook.service.CampaignExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaign-execution")
public class CampaignExecutionController {

    private final CampaignExecutionService service;

    public CampaignExecutionController(
            CampaignExecutionService service) {

        this.service = service;
    }

    @PostMapping("/execute/{campaignId}")
    public ResponseEntity<CampaignExecutionResponseDto>
    executeCampaign(
            @PathVariable Long campaignId){

        return ResponseEntity.ok(
                service.executeCampaign(
                        campaignId
                )
        );
    }
}