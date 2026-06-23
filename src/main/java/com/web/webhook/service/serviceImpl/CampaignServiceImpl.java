package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.CampaignRequestDto;
import com.web.webhook.dto.responseDto.CampaignResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.service.CampaignService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public CampaignResponseDto createCampaign(CampaignRequestDto requestDto) {

        Campaign campaign = new Campaign();
        campaign.setCampaignName(requestDto.getCampaignName());
        campaign.setTemplateId(requestDto.getTemplateId());
        campaign.setGroupId(requestDto.getGroupId());
        campaign.setScheduledAt(requestDto.getScheduledAt());

        campaign.setStatus("CREATED");
        campaign.setSentCount(0);
        campaign.setDeliveredCount(0);
        campaign.setFailedCount(0);
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setUpdatedAt(LocalDateTime.now());

        Campaign saved = campaignRepository.save(campaign);

        return mapToResponse(saved);
    }

    @Override
    public List<CampaignResponseDto> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CampaignResponseDto getCampaignById(Long id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign Not Found"));

        return mapToResponse(campaign);
    }

    @Override
    public CampaignResponseDto updateCampaign(Long id, CampaignRequestDto requestDto) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign Not Found"));

        campaign.setCampaignName(requestDto.getCampaignName());
        campaign.setTemplateId(requestDto.getTemplateId());
        campaign.setGroupId(requestDto.getGroupId());
        campaign.setScheduledAt(requestDto.getScheduledAt());
        campaign.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(campaignRepository.save(campaign));
    }

    @Override
    public CampaignResponseDto patchCampaign(Long id, CampaignRequestDto requestDto) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign Not Found"));

        if (requestDto.getCampaignName() != null)
            campaign.setCampaignName(requestDto.getCampaignName());

        if (requestDto.getTemplateId() != null)
            campaign.setTemplateId(requestDto.getTemplateId());

        if (requestDto.getGroupId() != null)
            campaign.setGroupId(requestDto.getGroupId());

        if (requestDto.getScheduledAt() != null)
            campaign.setScheduledAt(requestDto.getScheduledAt());

        campaign.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {

        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign Not Found"));

        campaignRepository.delete(campaign);
    }

    private CampaignResponseDto mapToResponse(Campaign campaign) {

        CampaignResponseDto dto = new CampaignResponseDto();

        dto.setId(campaign.getId());
        dto.setCampaignName(campaign.getCampaignName());
        dto.setTemplateId(campaign.getTemplateId());
        dto.setGroupId(campaign.getGroupId());
        dto.setStatus(campaign.getStatus());
        dto.setTotalContacts(campaign.getTotalContacts());
        dto.setSentCount(campaign.getSentCount());
        dto.setDeliveredCount(campaign.getDeliveredCount());
        dto.setFailedCount(campaign.getFailedCount());
        dto.setScheduledAt(campaign.getScheduledAt());

        return dto;
    }
}
