package com.web.webhook.service.serviceImpl;

import com.web.webhook.config.StreamConstants;
import com.web.webhook.service.RedisProducerService;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisProducerServiceImpl
        implements RedisProducerService {

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisProducerServiceImpl(
            RedisTemplate<String,Object> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publishWebhook(String payload) {

        System.out.println("========== PRODUCER START ==========");

        Map<String,String> map = new HashMap<>();

        map.put("payload", payload);

        RecordId recordId =
                redisTemplate.opsForStream()
                        .add(
                                StreamRecords
                                        .mapBacked(map)
                                        .withStreamKey(
                                                StreamConstants.WEBHOOK_STREAM
                                        )
                        );

        System.out.println("RECORD ID = " + recordId);

        System.out.println("========== PRODUCER END ==========");
    }
}