package com.web.webhook.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisStreamConfig {

    @Bean
    public CommandLineRunner createGroup(
            RedisTemplate<String,Object> redisTemplate){

        return args -> {

            try {

                redisTemplate.opsForStream()
                        .createGroup(
                                StreamConstants.WEBHOOK_STREAM,
                                ReadOffset.latest(),
                                StreamConstants.WEBHOOK_GROUP
                        );

                System.out.println("Group Created");

            } catch (Exception e){

                System.out.println("Group Exists");
            }
        };
    }
}