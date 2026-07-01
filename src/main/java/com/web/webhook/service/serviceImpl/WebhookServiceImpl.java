package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.dto.responseDto.WebhookResponseDto;
import com.web.webhook.entity.Campaign;
import com.web.webhook.entity.WebhookLog;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.CampaignRepository;
import com.web.webhook.repository.WebhookLogRepository;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.AutoReplyService;
import com.web.webhook.service.MetaApiService;
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

    // NEW: auto reply ke liye inject kiya
    private final AutoReplyService autoReplyService;

    // NEW: incoming message ka reply Meta ko bhejne ke liye
    private final MetaApiService metaApiService;

    public WebhookServiceImpl(
            WebhookLogRepository webhookLogRepository,
            WhatsappMessageRepository whatsappMessageRepository,
            CampaignRepository campaignRepository,
            AutoReplyService autoReplyService,
            MetaApiService metaApiService) {

        this.webhookLogRepository = webhookLogRepository;
        this.whatsappMessageRepository = whatsappMessageRepository;
        this.campaignRepository = campaignRepository;
        this.autoReplyService = autoReplyService;
        this.metaApiService = metaApiService;
    }

    @Override
    public void processWebhook(WebhookRequestDto dto, String rawPayload) {

        if (dto.getEntry() == null) return;

        for (WebhookRequestDto.Entry entry : dto.getEntry()) {

            if (entry.getChanges() == null) continue;

            for (WebhookRequestDto.Change change : entry.getChanges()) {

                WebhookRequestDto.Value value = change.getValue();
                if (value == null) continue;

                // existing logic — delivery status handle karo, same as before
                if (value.getStatuses() != null) {

                    for (WebhookRequestDto.Status status : value.getStatuses()) {

                        WebhookLog existing = webhookLogRepository
                                .findByMessageIdAndStatus(status.getId(), status.getStatus())
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

                        updateCampaignCounters(status.getId(), status.getStatus());
                    }
                }

                // existing logic — incoming messages handle karo
                if (value.getMessages() != null) {

                    for (WebhookRequestDto.Message message : value.getMessages()) {

                        WebhookLog log = new WebhookLog();
                        log.setRawPayload(rawPayload);
                        log.setMessageId(message.getId());
                        log.setStatus("incoming");
                        log.setPhoneNumber(message.getFrom());
                        log.setReceivedAt(LocalDateTime.now());
                        webhookLogRepository.save(log);

                        System.out.println("[WEBHOOK] Incoming message received from: "
                                + message.getFrom());

                        // NEW: auto reply trigger karo
                        handleAutoReply(message);
                    }
                }
            }
        }
    }

    // NEW: incoming message ka keyword match karke reply bhejta hai
    private void handleAutoReply(
            WebhookRequestDto.Message message) {

        try {

            // incoming message ka actual text body
            // Meta ka format: message.text.body
            String incomingText =
                    (message.getText() != null)
                            ? message.getText().getBody()
                            : null;

            if (incomingText == null) {
                System.out.println("[AUTO REPLY] Incoming message has no text body. Skipping. From: "
                        + message.getFrom());
                return;
            }

            String matchedReply =
                    autoReplyService.findMatchingReply(incomingText);

            if (matchedReply == null) {
                System.out.println("[AUTO REPLY] No rule matched for incoming message from: "
                        + message.getFrom());
                return;
            }

            // dummy WhatsappMessage banao reply bhejne ke liye
            WhatsappMessage replyMessage = new WhatsappMessage();
            replyMessage.setPhoneNumber(message.getFrom());
            replyMessage.setMessageBody(matchedReply);
            replyMessage.setCreatedBy("SYSTEM_AUTO_REPLY");

            String metaMessageId =
                    metaApiService.sendMessage(replyMessage);

            if (metaMessageId != null) {
                System.out.println("[AUTO REPLY] Reply sent to: "
                        + message.getFrom() + " | Reply: " + matchedReply);
            } else {
                System.err.println("[AUTO REPLY] Failed to send reply to: "
                        + message.getFrom());
            }

        } catch (Exception e) {
            System.err.println("[AUTO REPLY] handleAutoReply error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCampaignCounters(String metaMessageId, String status) {

        try {

            Optional<WhatsappMessage> optionalMessage =
                    whatsappMessageRepository.findByMetaMessageId(metaMessageId);

            if (optionalMessage.isEmpty()) {
                System.out.println("[WEBHOOK] No message found for Meta message ID: " + metaMessageId);
                return;
            }

            WhatsappMessage message = optionalMessage.get();

            message.setStatus(status.toUpperCase());
            message.setUpdatedAt(LocalDateTime.now());
            whatsappMessageRepository.save(message);

            if (message.getCampaignId() == null) return;

            Optional<Campaign> optionalCampaign =
                    campaignRepository.findById(message.getCampaignId());

            if (optionalCampaign.isEmpty()) return;

            Campaign campaign = optionalCampaign.get();

            switch (status.toLowerCase()) {

                case "sent":
                    int sc = campaign.getSentCount() == null ? 0 : campaign.getSentCount();
                    campaign.setSentCount(sc + 1);
                    break;

                case "delivered":
                    int dc = campaign.getDeliveredCount() == null ? 0 : campaign.getDeliveredCount();
                    campaign.setDeliveredCount(dc + 1);
                    break;

                case "read":
                    break;

                case "failed":
                    int fc = campaign.getFailedCount() == null ? 0 : campaign.getFailedCount();
                    campaign.setFailedCount(fc + 1);
                    break;
            }

            int total = campaign.getTotalContacts() == null ? 0 : campaign.getTotalContacts();
            int sent = campaign.getSentCount() == null ? 0 : campaign.getSentCount();
            int failed = campaign.getFailedCount() == null ? 0 : campaign.getFailedCount();

            if (total > 0 && (sent + failed) >= total) {
                campaign.setStatus("COMPLETED");
                System.out.println("[WEBHOOK] Campaign completed. Campaign ID: " + campaign.getId());
            }

            campaign.setUpdatedAt(LocalDateTime.now());
            campaignRepository.save(campaign);

            System.out.println("[WEBHOOK] Campaign counter updated."
                    + " Campaign ID: " + campaign.getId()
                    + " | Status: " + status
                    + " | Sent: " + campaign.getSentCount()
                    + " | Delivered: " + campaign.getDeliveredCount()
                    + " | Failed: " + campaign.getFailedCount());

        } catch (Exception e) {
            System.err.println("[WEBHOOK] updateCampaignCounters error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<WebhookResponseDto> getAllLogs() {

        List<WebhookLog> logs = webhookLogRepository.findAll();
        List<WebhookResponseDto> result = new ArrayList<>();

        for (WebhookLog log : logs) {

            WebhookResponseDto dto = new WebhookResponseDto();
            dto.setId(log.getId());
            dto.setMessageId(log.getMessageId());
            dto.setStatus(log.getStatus());
            dto.setPhoneNumber(log.getPhoneNumber());
            dto.setReceivedAt(log.getReceivedAt() != null
                    ? log.getReceivedAt().toString() : null);
            result.add(dto);
        }

        return result;
    }
}