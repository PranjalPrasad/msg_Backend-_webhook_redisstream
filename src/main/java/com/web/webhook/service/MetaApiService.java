package com.web.webhook.service;

import com.web.webhook.entity.WhatsappMessage;

public interface MetaApiService {

    String sendMessage(WhatsappMessage message);
}