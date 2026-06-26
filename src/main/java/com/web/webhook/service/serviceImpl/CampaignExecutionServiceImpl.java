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
    public CampaignExecutionResponseDto executeCampaign(
            Long campaignId) {

        // campaign fetch karo
        Campaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Campaign Not Found"
                                ));

        // campaign ka template fetch karo
        MessageTemplate template =
                templateRepository.findById(
                                campaign.getTemplateId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Template Not Found"
                                ));

        // campaign ke group ke saare contacts fetch karo
        List<ContactGroupMapping> mappings =
                mappingRepository.findByGroupId(
                        campaign.getGroupId()
                );

        int queuedCount = 0;

        for (ContactGroupMapping mapping : mappings) {

            // har contact fetch karo
            Contact contact =
                    contactRepository.findById(
                                    mapping.getContactId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Contact Not Found"
                                    ));

            // har contact ke liye ek WhatsappMessage banao
            WhatsappMessage message = new WhatsappMessage();

            // contact aur template link karo
            message.setContactId(contact.getId());
            message.setTemplateId(template.getId());

            // FIX: phoneNumber — contact ka actual number
            message.setPhoneNumber(contact.getPhoneNumber());

            // FIX: messageBody — template ka text
            message.setMessageBody(template.getTemplateBody());

            // FIX: campaignId — kaunse campaign ka message hai
            // yeh delivery webhook aane par campaign counter update karne ke liye chahiye
            message.setCampaignId(campaign.getId());

            // FIX: createdBy — kaunse user ne campaign banaya
            message.setCreatedBy(campaign.getCreatedBy());

            // FIX: timestamps
            message.setCreatedAt(LocalDateTime.now());
            message.setUpdatedAt(LocalDateTime.now());

            // status PENDING rakhte hain
            // jab Meta ko send hoga tab SENT hoga
            // jab delivery aayegi tab DELIVERED hoga
            message.setStatus("PENDING");

            // message database mein save karo
            WhatsappMessage savedMessage =
                    whatsappMessageRepository.save(message);

            // Redis stream mein sirf message ka ID bhejo
            // Consumer wahan se ID leke DB se message fetch karega
            // aur Meta ko bhejega
            try {
                redisProducerService.publishWebhook(
                        "MESSAGE_ID=" + savedMessage.getId()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            queuedCount++;
        }

        // campaign status PROCESSING karo
        campaign.setStatus("PROCESSING");
        campaign.setTotalContacts(queuedCount);
        campaign.setSentCount(0);
        campaign.setDeliveredCount(0);
        campaign.setFailedCount(0);
        campaign.setUpdatedAt(LocalDateTime.now());
        campaignRepository.save(campaign);

        // response banao
        CampaignExecutionResponseDto response =
                new CampaignExecutionResponseDto();

        response.setCampaignId(campaignId);
        response.setTotalContacts(queuedCount);
        response.setMessagesQueued(queuedCount);
        response.setStatus("PROCESSING");

        return response;
    }
}
