package com.web.webhook.service.serviceImpl;

import com.opencsv.CSVWriter;
import com.web.webhook.dto.responseDto.ReportResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.service.ReportService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final CampaignRepository campaignRepository;

    public ReportServiceImpl(
            CampaignRepository campaignRepository) {

        this.campaignRepository = campaignRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private ReportResponseDto convertToDto(Campaign campaign) {

        ReportResponseDto dto = new ReportResponseDto();
        dto.setCampaignId(campaign.getId());
        dto.setCampaignName(campaign.getCampaignName());
        dto.setStatus(campaign.getStatus());
        dto.setTotalContacts(campaign.getTotalContacts() != null
                ? campaign.getTotalContacts() : 0);
        dto.setSentCount(campaign.getSentCount() != null
                ? campaign.getSentCount() : 0);
        dto.setDeliveredCount(campaign.getDeliveredCount() != null
                ? campaign.getDeliveredCount() : 0);
        dto.setFailedCount(campaign.getFailedCount() != null
                ? campaign.getFailedCount() : 0);
        dto.setCreatedAt(campaign.getCreatedAt() != null
                ? campaign.getCreatedAt().toString() : null);

        return dto;
    }

    @Override
    public List<ReportResponseDto> getAllCampaignReports() {

        String email = getLoggedInUserEmail();

        List<Campaign> campaigns =
                campaignRepository.findByCreatedBy(email);

        List<ReportResponseDto> result = new ArrayList<>();

        for (Campaign campaign : campaigns) {
            result.add(convertToDto(campaign));
        }

        System.out.println("[REPORTS] All campaign reports fetched for: "
                + email + " | Count: " + result.size());

        return result;
    }

    @Override
    public ReportResponseDto getCampaignReport(Long campaignId) {

        String email = getLoggedInUserEmail();

        Campaign campaign = campaignRepository
                .findByIdAndCreatedBy(campaignId, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Campaign not found with id: " + campaignId
                        ));

        System.out.println("[REPORTS] Campaign report fetched: " + campaignId);

        return convertToDto(campaign);
    }

    @Override
    public byte[] exportAllCampaignsAsCsv() {

        String email = getLoggedInUserEmail();

        List<Campaign> campaigns =
                campaignRepository.findByCreatedBy(email);

        return generateCsv(campaigns);
    }

    @Override
    public byte[] exportCampaignAsCsv(Long campaignId) {

        String email = getLoggedInUserEmail();

        Campaign campaign = campaignRepository
                .findByIdAndCreatedBy(campaignId, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Campaign not found with id: " + campaignId
                        ));

        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(campaign);

        return generateCsv(campaigns);
    }

    private byte[] generateCsv(List<Campaign> campaigns) {

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            OutputStreamWriter writer =
                    new OutputStreamWriter(
                            outputStream,
                            StandardCharsets.UTF_8
                    );

            CSVWriter csvWriter = new CSVWriter(writer);

            // CSV header row
            String[] header = {
                    "Campaign ID",
                    "Campaign Name",
                    "Status",
                    "Total Contacts",
                    "Sent",
                    "Delivered",
                    "Failed",
                    "Created At"
            };

            csvWriter.writeNext(header);

            // CSV data rows
            for (Campaign campaign : campaigns) {

                String[] row = {
                        String.valueOf(campaign.getId()),
                        campaign.getCampaignName(),
                        campaign.getStatus(),
                        String.valueOf(campaign.getTotalContacts() != null
                                ? campaign.getTotalContacts() : 0),
                        String.valueOf(campaign.getSentCount() != null
                                ? campaign.getSentCount() : 0),
                        String.valueOf(campaign.getDeliveredCount() != null
                                ? campaign.getDeliveredCount() : 0),
                        String.valueOf(campaign.getFailedCount() != null
                                ? campaign.getFailedCount() : 0),
                        campaign.getCreatedAt() != null
                                ? campaign.getCreatedAt().toString() : ""
                };

                csvWriter.writeNext(row);
            }

            csvWriter.close();

            System.out.println("[REPORTS] CSV generated. Rows: "
                    + campaigns.size());

            return outputStream.toByteArray();

        } catch (Exception e) {
            System.err.println("[REPORTS] CSV generation error: "
                    + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate CSV: "
                    + e.getMessage());
        }
    }
}