package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.dto.responseDto.WebhookResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.entity.WebhookLog;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.repository.WebhookLogRepository;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.WebhookService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WebhookServiceImpl implements WebhookService {

    private final WebhookLogRepository webhookLogRepository;
    private final WhatsappMessageRepository whatsappMessageRepository;
    private final CampaignRepository campaignRepository;

    public WebhookServiceImpl(
            WebhookLogRepository webhookLogRepository,
            WhatsappMessageRepository whatsappMessageRepository,
            CampaignRepository campaignRepository) {

        this.webhookLogRepository = webhookLogRepository;
        this.whatsappMessageRepository = whatsappMessageRepository;
        this.campaignRepository = campaignRepository;
    }

    @Override
    public void processWebhook(
            WebhookRequestDto dto,
            String rawPayload) {

        if (dto.getEntry() == null) return;

        for (WebhookRequestDto.Entry entry : dto.getEntry()) {

            if (entry.getChanges() == null) continue;

            for (WebhookRequestDto.Change change : entry.getChanges()) {

                WebhookRequestDto.Value value = change.getValue();
                if (value == null) continue;

                // delivery / sent / read / failed status handle karo
                if (value.getStatuses() != null) {

                    for (WebhookRequestDto.Status status : value.getStatuses()) {

                        // step 1: WebhookLog save karo — existing logic same hai
                        WebhookLog existing =
                                webhookLogRepository
                                        .findByMessageIdAndStatus(
                                                status.getId(),
                                                status.getStatus())
                                        .orElse(null);

                        if (existing == null) {

                            WebhookLog log = new WebhookLog();
                            log.setRawPayload(rawPayload);
                            log.setMessageId(status.getId());
                            log.setStatus(status.getStatus());
                            log.setPhoneNumber(status.getRecipientId());
                            log.setReceivedAt(LocalDateTime.now());
                            webhookLogRepository.save(log);
                        }

                        // step 2: FIX — campaign counters update karo
                        // status.getId() = Meta ka wamid = hamara metaMessageId
                        updateCampaignCounters(
                                status.getId(),
                                status.getStatus()
                        );
                    }
                }

                // incoming messages handle karo — existing logic same hai
                if (value.getMessages() != null) {

                    for (WebhookRequestDto.Message message : value.getMessages()) {

                        WebhookLog log = new WebhookLog();
                        log.setRawPayload(rawPayload);
                        log.setMessageId(message.getId());
                        log.setStatus("incoming");
                        log.setPhoneNumber(message.getFrom());
                        log.setReceivedAt(LocalDateTime.now());
                        webhookLogRepository.save(log);
                    }
                }
            }
        }
    }

    // FIX: naya method — delivery aane par campaign counters update karta hai
    private void updateCampaignCounters(
            String metaMessageId,
            String status) {

        try {

            // step 1: metaMessageId se WhatsappMessage dhundho
            Optional<WhatsappMessage> optionalMessage =
                    whatsappMessageRepository
                            .findByMetaMessageId(metaMessageId);

            if (optionalMessage.isEmpty()) {
                // Meta configure hone se pehle DUMMY_WAMID hoga
                // toh match nahi milega — ye normal hai
                System.out.println(
                        "metaMessageId se message nahi mila: " + metaMessageId
                );
                return;
            }

            WhatsappMessage message = optionalMessage.get();

            // step 2: WhatsappMessage ka status update karo
            message.setStatus(status.toUpperCase());
            message.setUpdatedAt(LocalDateTime.now());
            whatsappMessageRepository.save(message);

            // step 3: campaign fetch karo
            if (message.getCampaignId() == null) return;

            Optional<Campaign> optionalCampaign =
                    campaignRepository.findById(
                            message.getCampaignId()
                    );

            if (optionalCampaign.isEmpty()) return;

            Campaign campaign = optionalCampaign.get();

            // step 4: status ke hisab se counter badhao
            switch (status.toLowerCase()) {

                case "sent":
                    int sc = campaign.getSentCount() == null
                            ? 0 : campaign.getSentCount();
                    campaign.setSentCount(sc + 1);
                    break;

                case "delivered":
                    int dc = campaign.getDeliveredCount() == null
                            ? 0 : campaign.getDeliveredCount();
                    campaign.setDeliveredCount(dc + 1);
                    break;

                case "read":
                    // read aata hai Meta se — alag counter nahi hai
                    // sirf WebhookLog mein saved hai
                    break;

                case "failed":
                    int fc = campaign.getFailedCount() == null
                            ? 0 : campaign.getFailedCount();
                    campaign.setFailedCount(fc + 1);
                    break;
            }

            // step 5: check karo kya campaign complete ho gaya
            // sent + failed == totalContacts matlab sab process ho gaye
            int total = campaign.getTotalContacts() == null
                    ? 0 : campaign.getTotalContacts();
            int sent = campaign.getSentCount() == null
                    ? 0 : campaign.getSentCount();
            int failed = campaign.getFailedCount() == null
                    ? 0 : campaign.getFailedCount();

            if (total > 0 && (sent + failed) >= total) {
                campaign.setStatus("COMPLETED");
            }

            campaign.setUpdatedAt(LocalDateTime.now());
            campaignRepository.save(campaign);

            System.out.println(
                    "Campaign " + campaign.getId()
                            + " counter update hua — status: " + status
            );

        } catch (Exception e) {
            System.err.println(
                    "updateCampaignCounters error: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    // existing method — same as before
    @Override
    public List<WebhookResponseDto> getAllLogs() {

        List<WebhookLog> logs =
                webhookLogRepository.findAll();

        List<WebhookResponseDto> result = new ArrayList<>();

        for (WebhookLog log : logs) {

            WebhookResponseDto dto = new WebhookResponseDto();
            dto.setId(log.getId());
            dto.setMessageId(log.getMessageId());
            dto.setStatus(log.getStatus());
            dto.setPhoneNumber(log.getPhoneNumber());
            dto.setReceivedAt(
                    log.getReceivedAt() != null
                            ? log.getReceivedAt().toString()
                            : null
            );
            result.add(dto);
        }

        return result;
    }
}
