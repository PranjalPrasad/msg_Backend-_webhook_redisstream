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
public class RedisConsumerServiceImpl
        implements RedisConsumerService {

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
                    redisTemplate.opsForStream()
                            .read(
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

                    String payload =
                            record.getValue()
                                    .get("payload")
                                    .toString();

                    System.out.println(
                            "CONSUMER RECEIVED: " + payload
                    );

                    // Redis mein 2 tarah ke messages aate hain:
                    //
                    // TYPE 1 — Campaign execution se:
                    //   format: "MESSAGE_ID=123"
                    //   kaam: Meta ko actual message bhejna hai
                    //
                    // TYPE 2 — Meta webhook se (WebhookController ne push kiya):
                    //   format: JSON string
                    //   kaam: delivery status save karna + campaign counter update
                    //
                    // dono ko alag alag handle karte hain

                    if (payload.startsWith("MESSAGE_ID=")) {

                        // TYPE 1: campaign message — Meta ko bhejna hai
                        handleCampaignMessage(payload);

                    } else {

                        // TYPE 2: Meta webhook event — status save karna hai
                        handleWebhookEvent(payload);
                    }

                    // message process ho gaya — acknowledge karo
                    redisTemplate.opsForStream()
                            .acknowledge(
                                    StreamConstants.WEBHOOK_STREAM,
                                    StreamConstants.WEBHOOK_GROUP,
                                    record.getId()
                            );

                    System.out.println("MESSAGE ACKNOWLEDGED");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TYPE 1 handler — campaign message Meta ko bhejna hai
    private void handleCampaignMessage(String payload) {

        try {

            // "MESSAGE_ID=123" se 123 nikalo
            Long messageId = Long.parseLong(
                    payload.replace("MESSAGE_ID=", "").trim()
            );

            // DB se message fetch karo
            Optional<WhatsappMessage> optional =
                    whatsappMessageRepository.findById(messageId);

            if (optional.isEmpty()) {
                System.out.println(
                        "Message nahi mila ID ke liye: " + messageId
                );
                return;
            }

            WhatsappMessage message = optional.get();

            System.out.println(
                    "MESSAGE BHEJ RAHA HAI: " + message.getPhoneNumber()
            );

            // Meta ko message bhejo
            // abhi DUMMY_WAMID return hoga
            // Meta configure hone ke baad actual wamid aayega
            String metaMessageId =
                    metaApiService.sendMessage(message);

            if (metaMessageId != null) {

                // wamid save karo — baad mein delivery webhook se match karega
                message.setMetaMessageId(metaMessageId);
                message.setStatus("SENT");
                message.setUpdatedAt(LocalDateTime.now());
                whatsappMessageRepository.save(message);

                System.out.println(
                        "MESSAGE SENT. Meta ID: " + metaMessageId
                );

            } else {

                // Meta se null aaya matlab error
                message.setStatus("FAILED");
                message.setUpdatedAt(LocalDateTime.now());
                whatsappMessageRepository.save(message);

                System.out.println(
                        "MESSAGE FAILED. ID: " + messageId
                );
            }

        } catch (Exception e) {
            System.err.println(
                    "handleCampaignMessage error: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    // TYPE 2 handler — Meta webhook event process karna hai
    // yeh existing logic same hai — sirf alag method mein daala hai
    private void handleWebhookEvent(String payload) {

        try {

            WebhookRequestDto dto =
                    objectMapper.readValue(
                            payload,
                            WebhookRequestDto.class
                    );

            System.out.println("WEBHOOK EVENT PROCESS HO RAHA HAI");

            webhookService.processWebhook(dto, payload);

            System.out.println("WEBHOOK EVENT PROCESS HUA");

        } catch (Exception e) {
            System.err.println(
                    "handleWebhookEvent error: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
