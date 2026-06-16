package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.dto.responseDto.WebhookResponseDto;
import com.web.webhook.entity.WebhookLog;
import com.web.webhook.repository.WebhookLogRepository;
import com.web.webhook.service.WebhookService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebhookServiceImpl implements WebhookService {

    private final WebhookLogRepository webhookLogRepository;

    public WebhookServiceImpl(WebhookLogRepository webhookLogRepository) {
        this.webhookLogRepository = webhookLogRepository;
    }

    @Override
    public void processWebhook(WebhookRequestDto dto, String rawPayload) {
        if (dto.getEntry() == null) return;

        for (WebhookRequestDto.Entry entry : dto.getEntry()) {
            if (entry.getChanges() == null) continue;

            for (WebhookRequestDto.Change change : entry.getChanges()) {
                WebhookRequestDto.Value value = change.getValue();
                if (value == null) continue;

                // Handle delivery status updates
                if (value.getStatuses() != null) {
                    for (WebhookRequestDto.Status status : value.getStatuses()) {
                        WebhookLog log = new WebhookLog();
                        log.setRawPayload(rawPayload);
                        log.setMessageId(status.getId());
                        log.setStatus(status.getStatus());
                        log.setPhoneNumber(status.getRecipientId());
                        log.setReceivedAt(LocalDateTime.now());
                        WebhookLog existing =
                                webhookLogRepository
                                        .findByMessageIdAndStatus(
                                                status.getId(),
                                                status.getStatus())
                                        .orElse(null);

                        if(existing == null){
                            webhookLogRepository.save(log);
                        }
                    }
                }

                // Handle incoming messages
                if (value.getMessages() != null) {
                    for (WebhookRequestDto.Message message : value.getMessages()) {
                        WebhookLog log = new WebhookLog();
                        log.setRawPayload(rawPayload);
                        log.setMessageId(message.getId());
                        log.setStatus("incoming");
                        log.setPhoneNumber(message.getFrom());
                        log.setReceivedAt(LocalDateTime.now());
                        webhookLogRepository.save(log);
                    }
                }
            }
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
            dto.setReceivedAt(log.getReceivedAt() != null ? log.getReceivedAt().toString() : null);
            result.add(dto);
        }

        return result;
    }
}
