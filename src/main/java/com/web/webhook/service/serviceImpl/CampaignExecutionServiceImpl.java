package com.web.webhook.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.webhook.dto.responseDto.CampaignExecutionResponseDto;
import com.web.webhook.entity.*;
import com.web.webhook.repository.*;
import com.web.webhook.service.CampaignExecutionService;
import com.web.webhook.service.RedisProducerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignExecutionServiceImpl
        implements CampaignExecutionService {

    private final CampaignRepository campaignRepository;
    private final ContactGroupMappingRepository mappingRepository;
    private final ContactRepository contactRepository;
    private final MessageTemplateRepository templateRepository;
    private final WhatsappMessageRepository whatsappMessageRepository;
    private final RedisProducerService redisProducerService;

    public CampaignExecutionServiceImpl(
            CampaignRepository campaignRepository,
            ContactGroupMappingRepository mappingRepository,
            ContactRepository contactRepository,
            MessageTemplateRepository templateRepository,
            WhatsappMessageRepository whatsappMessageRepository,
            RedisProducerService redisProducerService) {

        this.campaignRepository = campaignRepository;
        this.mappingRepository = mappingRepository;
        this.contactRepository = contactRepository;
        this.templateRepository = templateRepository;
        this.whatsappMessageRepository = whatsappMessageRepository;
        this.redisProducerService = redisProducerService;
    }

    @Override
    public CampaignExecutionResponseDto executeCampaign(
            Long campaignId) {

        Campaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Campaign Not Found"
                                ));

        MessageTemplate template =
                templateRepository.findById(
                                campaign.getTemplateId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Template Not Found"
                                ));

        List<ContactGroupMapping> mappings =
                mappingRepository.findByGroupId(
                        campaign.getGroupId()
                );

        int queuedCount = 0;

        for(ContactGroupMapping mapping : mappings){

            Contact contact =
                    contactRepository.findById(
                                    mapping.getContactId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Contact Not Found"
                                    ));

            WhatsappMessage message =
                    new WhatsappMessage();

            message.setContactId(
                    contact.getId()
            );

            message.setTemplateId(
                    template.getId()
            );

            message.setStatus(
                    "PENDING"
            );

            WhatsappMessage savedMessage =
                    whatsappMessageRepository.save(
                            message
                    );

            try {

                ObjectMapper mapper =
                        new ObjectMapper();

                String payload =
                        mapper.writeValueAsString(
                                savedMessage
                        );

                redisProducerService.publishWebhook(
                        payload
                );

            } catch (Exception e) {

                e.printStackTrace();
            }

            queuedCount++;
        }

        campaign.setStatus(
                "PROCESSING"
        );

        campaign.setTotalContacts(
                queuedCount
        );

        campaignRepository.save(
                campaign
        );

        CampaignExecutionResponseDto response =
                new CampaignExecutionResponseDto();

        response.setCampaignId(
                campaignId
        );

        response.setTotalContacts(
                queuedCount
        );

        response.setMessagesQueued(
                queuedCount
        );

        response.setStatus(
                "PROCESSING"
        );

        return response;
    }
}