package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.CampaignRequestDto;
import com.web.webhook.dto.responseDto.CampaignResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.entity.Contact;
import com.web.webhook.entity.ContactGroup;
import com.web.webhook.entity.ContactGroupMapping;
import com.web.webhook.entity.MessageTemplate;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.repository.ContactGroupMappingRepository;
import com.web.webhook.repository.ContactGroupRepository;
import com.web.webhook.repository.ContactRepository;
import com.web.webhook.repository.MessageTemplateRepository;
import com.web.webhook.service.CampaignService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignServiceImpl
        implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final ContactGroupRepository groupRepository;
    private final ContactGroupMappingRepository mappingRepository;
    private final ContactRepository contactRepository;
    private final MessageTemplateRepository templateRepository;

    public CampaignServiceImpl(
            CampaignRepository campaignRepository,
            ContactGroupRepository groupRepository,
            ContactGroupMappingRepository mappingRepository,
            ContactRepository contactRepository,
            MessageTemplateRepository templateRepository) {

        this.campaignRepository = campaignRepository;
        this.groupRepository = groupRepository;
        this.mappingRepository = mappingRepository;
        this.contactRepository = contactRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    public CampaignResponseDto createCampaign(
            CampaignRequestDto requestDto) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        if (campaignRepository.existsByCampaignNameAndCreatedBy(
                requestDto.getCampaignName(),
                email)) {

            throw new RuntimeException(
                    "Campaign already exists.");
        }

        ContactGroup group =
                groupRepository
                        .findByIdAndCreatedBy(
                                requestDto.getGroupId(),
                                email)
                        .orElseThrow(() ->
                                new RuntimeException("Group not found"));

        MessageTemplate template =
                templateRepository
                        .findByIdAndCreatedBy(
                                requestDto.getTemplateId(),
                                email)
                        .orElseThrow(() ->
                                new RuntimeException("Template not found"));

        List<ContactGroupMapping> mappings =
                mappingRepository.findByGroupId(group.getId());

        List<Long> contactIds =
                mappings.stream()
                        .map(ContactGroupMapping::getContactId)
                        .collect(Collectors.toList());

        List<Contact> contacts =
                contactRepository.findByIdIn(contactIds);

        Campaign campaign = new Campaign();

        campaign.setCampaignName(
                requestDto.getCampaignName());

        campaign.setTemplateId(
                template.getId());

        campaign.setGroupId(
                group.getId());

        campaign.setScheduledAt(
                requestDto.getScheduledAt());

        campaign.setTotalContacts(
                contacts.size());

        campaign.setSentCount(0);
        campaign.setDeliveredCount(0);
        campaign.setFailedCount(0);

        campaign.setStatus("CREATED");

        campaign.setCreatedBy(email);

        campaign.setCreatedAt(
                LocalDateTime.now());

        campaign.setUpdatedAt(
                LocalDateTime.now());

        Campaign saved =
                campaignRepository.save(campaign);

        return map(saved);
    }

    @Override
    public List<CampaignResponseDto> getAllCampaigns() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return campaignRepository
                .findByCreatedBy(email)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public CampaignResponseDto getCampaignById(
            Long id) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Campaign campaign =
                campaignRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(() ->
                                new RuntimeException("Campaign not found"));

        return map(campaign);
    }

    @Override
    public CampaignResponseDto updateCampaign(
            Long id,
            CampaignRequestDto requestDto) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Campaign campaign =
                campaignRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(() ->
                                new RuntimeException("Campaign not found"));

        campaign.setCampaignName(
                requestDto.getCampaignName());

        campaign.setTemplateId(
                requestDto.getTemplateId());

        campaign.setGroupId(
                requestDto.getGroupId());

        campaign.setScheduledAt(
                requestDto.getScheduledAt());

        campaign.setUpdatedAt(
                LocalDateTime.now());

        return map(
                campaignRepository.save(campaign));
    }

    @Override
    public CampaignResponseDto patchCampaign(
            Long id,
            CampaignRequestDto requestDto) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Campaign campaign =
                campaignRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(() ->
                                new RuntimeException("Campaign not found"));

        if (requestDto.getCampaignName() != null)
            campaign.setCampaignName(requestDto.getCampaignName());

        if (requestDto.getTemplateId() != null)
            campaign.setTemplateId(requestDto.getTemplateId());

        if (requestDto.getGroupId() != null)
            campaign.setGroupId(requestDto.getGroupId());

        if (requestDto.getScheduledAt() != null)
            campaign.setScheduledAt(requestDto.getScheduledAt());

        campaign.setUpdatedAt(LocalDateTime.now());

        return map(
                campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(
            Long id) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        Campaign campaign =
                campaignRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(() ->
                                new RuntimeException("Campaign not found"));

        campaignRepository.delete(campaign);
    }

    private CampaignResponseDto map(
            Campaign campaign) {

        CampaignResponseDto dto =
                new CampaignResponseDto();

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
        dto.setCreatedBy(campaign.getCreatedBy());

        return dto;
    }
}