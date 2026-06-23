package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.CampaignRequestDto;
import com.web.webhook.dto.responseDto.CampaignResponseDto;
import com.web.webhook.service.CampaignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping("/create-campaign")
    public ResponseEntity<CampaignResponseDto> createCampaign(
            @RequestBody CampaignRequestDto requestDto) {

        return ResponseEntity.ok(
                campaignService.createCampaign(requestDto)
        );
    }

    @GetMapping("/get-all-campaigns")
    public ResponseEntity<List<CampaignResponseDto>> getAll() {

        return ResponseEntity.ok(
                campaignService.getAllCampaigns()
        );
    }

    @GetMapping("/get-campaign-by-id/{id}")
    public ResponseEntity<CampaignResponseDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                campaignService.getCampaignById(id)
        );
    }

    @PutMapping("/update-campaign/{id}")
    public ResponseEntity<CampaignResponseDto> update(
            @PathVariable Long id,
            @RequestBody CampaignRequestDto requestDto) {

        return ResponseEntity.ok(
                campaignService.updateCampaign(id, requestDto)
        );
    }

    @PatchMapping("/patch-campaign/{id}")
    public ResponseEntity<CampaignResponseDto> patch(
            @PathVariable Long id,
            @RequestBody CampaignRequestDto requestDto) {

        return ResponseEntity.ok(
                campaignService.patchCampaign(id, requestDto)
        );
    }

    @DeleteMapping("/delete-campaign/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        campaignService.deleteCampaign(id);
        return ResponseEntity.ok("Campaign Deleted Successfully");
    }
}