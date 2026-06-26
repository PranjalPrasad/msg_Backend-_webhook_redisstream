package com.web.webhook.service;

import com.web.webhook.dto.requestDto.CampaignRequestDto;
import com.web.webhook.dto.responseDto.CampaignResponseDto;

import java.util.List;

public interface CampaignService {

    CampaignResponseDto createCampaign(
            CampaignRequestDto requestDto);

    List<CampaignResponseDto> getAllCampaigns();

    CampaignResponseDto getCampaignById(
            Long id);

    CampaignResponseDto updateCampaign(
            Long id,
            CampaignRequestDto requestDto);

    CampaignResponseDto patchCampaign(
            Long id,
            CampaignRequestDto requestDto);

    void deleteCampaign(
            Long id);
}