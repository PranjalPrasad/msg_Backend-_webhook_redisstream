package com.web.webhook.service;

public interface RedisProducerService {

    void publishWebhook(String payload);
}
