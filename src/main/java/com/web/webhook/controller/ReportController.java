package com.web.webhook.controller;

import com.web.webhook.dto.responseDto.ReportResponseDto;
import com.web.webhook.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // saare campaigns ka report (JSON)
    @GetMapping
    public ResponseEntity<List<ReportResponseDto>>
    getAllCampaignReports() {

        return ResponseEntity.ok(
                reportService.getAllCampaignReports()
        );
    }

    // specific campaign ka report (JSON)
    @GetMapping("/{campaignId}")
    public ResponseEntity<ReportResponseDto>
    getCampaignReport(
            @PathVariable Long campaignId) {

        return ResponseEntity.ok(
                reportService.getCampaignReport(campaignId)
        );
    }

    // saare campaigns ka CSV export
    @GetMapping("/export")
    public ResponseEntity<byte[]>
    exportAllCampaignsAsCsv() {

        byte[] csvBytes =
                reportService.exportAllCampaignsAsCsv();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "text/csv"
        ));
        headers.setContentDispositionFormData(
                "attachment",
                "campaigns_report.csv"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }

    // specific campaign ka CSV export
    @GetMapping("/export/{campaignId}")
    public ResponseEntity<byte[]>
    exportCampaignAsCsv(
            @PathVariable Long campaignId) {

        byte[] csvBytes =
                reportService.exportCampaignAsCsv(campaignId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "text/csv"
        ));
        headers.setContentDispositionFormData(
                "attachment",
                "campaign_" + campaignId + "_report.csv"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}