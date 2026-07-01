package com.web.webhook.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.webhook.config.StreamConstants;
import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.MetaApiService;
import com.web.webhook.service.RedisConsumerService;
import com.web.webhook.service.WebhookService;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RedisConsumerServiceImpl implements RedisConsumerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final WebhookService webhookService;
    private final MetaApiService metaApiService;
    private final WhatsappMessageRepository whatsappMessageRepository;

    public RedisConsumerServiceImpl(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper,
            WebhookService webhookService,
            MetaApiService metaApiService,
            WhatsappMessageRepository whatsappMessageRepository) {

        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.webhookService = webhookService;
        this.metaApiService = metaApiService;
        this.whatsappMessageRepository = whatsappMessageRepository;
    }

    @Scheduled(fixedDelay = 1000)
    public void consume() {

        try {

            List<MapRecord<String, Object, Object>> records =
                    redisTemplate.opsForStream().read(
                            Consumer.from(
                                    StreamConstants.WEBHOOK_GROUP,
                                    StreamConstants.CONSUMER_NAME
                            ),
                            StreamReadOptions.empty().count(10),
                            StreamOffset.create(
                                    StreamConstants.WEBHOOK_STREAM,
                                    ReadOffset.lastConsumed()
                            )
                    );

            if (records == null || records.isEmpty()) {
                return;
            }

            for (MapRecord<String, Object, Object> record : records) {

                try {

                    String payload = record.getValue().get("payload").toString();

                    System.out.println("[CONSUMER] Received payload: " + payload);

                    if (payload.startsWith("MESSAGE_ID=")) {
                        handleCampaignMessage(payload);
                    } else {
                        handleWebhookEvent(payload);
                    }

                    redisTemplate.opsForStream().acknowledge(
                            StreamConstants.WEBHOOK_STREAM,
                            StreamConstants.WEBHOOK_GROUP,
                            record.getId()
                    );

                    System.out.println("[CONSUMER] Message acknowledged. Record ID: " + record.getId());

                } catch (Exception e) {
                    System.err.println("[CONSUMER] Error processing record: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.err.println("[CONSUMER] Error reading from stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleCampaignMessage(String payload) {

        try {

            Long messageId = Long.parseLong(payload.replace("MESSAGE_ID=", "").trim());

            Optional<WhatsappMessage> optional = whatsappMessageRepository.findById(messageId);

            if (optional.isEmpty()) {
                System.err.println("[CONSUMER] Message not found for ID: " + messageId);
                return;
            }

            WhatsappMessage message = optional.get();

            System.out.println("[CONSUMER] Sending message to: " + message.getPhoneNumber()
                    + " | Message ID: " + messageId);

            String metaMessageId = metaApiService.sendMessage(message);

            if (metaMessageId != null) {

                message.setMetaMessageId(metaMessageId);
                message.setStatus("SENT");
                message.setUpdatedAt(LocalDateTime.now());
                whatsappMessageRepository.save(message);

                System.out.println("[CONSUMER] Message sent successfully."
                        + " Message ID: " + messageId
                        + " | Meta WAMID: " + metaMessageId);

            } else {

                message.setStatus("FAILED");
                message.setUpdatedAt(LocalDateTime.now());
                whatsappMessageRepository.save(message);

                System.err.println("[CONSUMER] Message sending failed. Message ID: " + messageId);
            }

        } catch (Exception e) {
            System.err.println("[CONSUMER] handleCampaignMessage error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleWebhookEvent(String payload) {

        try {

            WebhookRequestDto dto = objectMapper.readValue(payload, WebhookRequestDto.class);

            System.out.println("[CONSUMER] Processing webhook event.");

            webhookService.processWebhook(dto, payload);

            System.out.println("[CONSUMER] Webhook event processed successfully.");

        } catch (Exception e) {
            System.err.println("[CONSUMER] handleWebhookEvent error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}