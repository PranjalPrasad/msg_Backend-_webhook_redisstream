package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.responseDto.CampaignExecutionResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.entity.Contact;
import com.web.webhook.entity.ContactGroupMapping;
import com.web.webhook.entity.MessageTemplate;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.repository.ContactGroupMappingRepository;
import com.web.webhook.repository.ContactRepository;
import com.web.webhook.repository.MessageTemplateRepository;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.CampaignExecutionService;
import com.web.webhook.service.RedisProducerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public CampaignExecutionResponseDto executeCampaign(Long campaignId) {

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + campaignId));

        MessageTemplate template = templateRepository.findById(campaign.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + campaign.getTemplateId()));

        List<ContactGroupMapping> mappings = mappingRepository.findByGroupId(campaign.getGroupId());

        int queuedCount = 0;

        for (ContactGroupMapping mapping : mappings) {

            Contact contact = contactRepository.findById(mapping.getContactId())
                    .orElseThrow(() -> new RuntimeException("Contact not found with id: " + mapping.getContactId()));

            WhatsappMessage message = new WhatsappMessage();
            message.setContactId(contact.getId());
            message.setTemplateId(template.getId());
            message.setPhoneNumber(contact.getPhoneNumber());
            message.setMessageBody(template.getTemplateBody());
            message.setCampaignId(campaign.getId());
            message.setCreatedBy(campaign.getCreatedBy());
            message.setStatus("PENDING");
            message.setCreatedAt(LocalDateTime.now());
            message.setUpdatedAt(LocalDateTime.now());

            WhatsappMessage savedMessage = whatsappMessageRepository.save(message);

            try {
                redisProducerService.publishWebhook("MESSAGE_ID=" + savedMessage.getId());
            } catch (Exception e) {
                System.err.println("Failed to push message to Redis. Message ID: " + savedMessage.getId());
                e.printStackTrace();
            }

            queuedCount++;
        }

        campaign.setStatus("PROCESSING");
        campaign.setTotalContacts(queuedCount);
        campaign.setSentCount(0);
        campaign.setDeliveredCount(0);
        campaign.setFailedCount(0);
        campaign.setUpdatedAt(LocalDateTime.now());
        campaignRepository.save(campaign);

        System.out.println("Campaign started. Campaign ID: " + campaignId + " | Total contacts queued: " + queuedCount);

        CampaignExecutionResponseDto response = new CampaignExecutionResponseDto();
        response.setCampaignId(campaignId);
        response.setTotalContacts(queuedCount);
        response.setMessagesQueued(queuedCount);
        response.setStatus("PROCESSING");

        return response;
    }
}