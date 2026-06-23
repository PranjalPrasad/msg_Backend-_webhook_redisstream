package com.web.webhook.config;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisStreamInitializer {

    private final RedisConnectionFactory connectionFactory;

    public RedisStreamInitializer(
            RedisConnectionFactory connectionFactory) {

        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    public void init() {

        try {

            RedisConnection connection =
                    connectionFactory.getConnection();

            try {

                connection.xGroupCreate(
                        StreamConstants.WEBHOOK_STREAM.getBytes(),
                        StreamConstants.WEBHOOK_GROUP,
                        org.springframework.data.redis.connection.stream.ReadOffset.latest(),
                        true
                );

                System.out.println(
                        "STREAM GROUP CREATED"
                );

            } catch (Exception e) {

                System.out.println(
                        "GROUP ALREADY EXISTS"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}