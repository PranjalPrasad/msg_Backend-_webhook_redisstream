package com.web.webhook.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.webhook.config.StreamConstants;
import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.service.RedisConsumerService;
import com.web.webhook.service.WebhookService;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisConsumerServiceImpl
        implements RedisConsumerService {

    private final RedisTemplate<String,Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final WebhookService webhookService;

    public RedisConsumerServiceImpl(
            RedisTemplate<String,Object> redisTemplate,
            ObjectMapper objectMapper,
            WebhookService webhookService) {

        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.webhookService = webhookService;
    }

    @Scheduled(fixedDelay = 1000)
    public void consume() {

        try {

            List<MapRecord<String,Object,Object>> records =
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

            if(records == null || records.isEmpty()){
                return;
            }


            for(MapRecord<String,Object,Object> record : records){

                try {
                    System.out.println(
                            "MESSAGE RECEIVED FROM STREAM"
                    );

                    String payload =
                            record.getValue()
                                    .get("payload")
                                    .toString();

                    System.out.println(
                            "PAYLOAD = " + payload
                    );

                    WebhookRequestDto dto =
                            objectMapper.readValue(
                                    payload,
                                    WebhookRequestDto.class);
                    System.out.println(
                            "CALLING WEBHOOK SERVICE"
                    );

                    webhookService.processWebhook(
                            dto,
                            payload
                    );
                    System.out.println(
                            "WEBHOOK SERVICE SUCCESS"
                    );

                    redisTemplate.opsForStream()
                            .acknowledge(
                                    StreamConstants.WEBHOOK_STREAM,
                                    StreamConstants.WEBHOOK_GROUP,
                                    record.getId()
                            );

                    System.out.println(
                            "MESSAGE ACKNOWLEDGED"
                    );

                } catch (Exception e){

                    System.out.println(
                            e.getMessage()
                    );
                }
            }




        } catch (Exception e){

            System.out.println(
                    e.getMessage()
            );
        }
    }
}
