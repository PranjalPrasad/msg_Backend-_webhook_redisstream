package com.web.webhook.service;

import com.web.webhook.dto.responseDto.ReportResponseDto;

import java.util.List;

public interface ReportService {

    // saare campaigns ka report
    List<ReportResponseDto> getAllCampaignReports();

    // specific campaign ka report
    ReportResponseDto getCampaignReport(Long campaignId);

    // saare campaigns ka CSV export
    byte[] exportAllCampaignsAsCsv();

    // specific campaign ka CSV export
    byte[] exportCampaignAsCsv(Long campaignId);
}