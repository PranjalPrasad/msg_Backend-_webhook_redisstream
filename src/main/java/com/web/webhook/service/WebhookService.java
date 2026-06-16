package com.web.webhook.service;

import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.dto.responseDto.WebhookResponseDto;

import java.util.List;

public interface WebhookService {

    void processWebhook(WebhookRequestDto dto, String rawPayload);

    List<WebhookResponseDto> getAllLogs();
}
