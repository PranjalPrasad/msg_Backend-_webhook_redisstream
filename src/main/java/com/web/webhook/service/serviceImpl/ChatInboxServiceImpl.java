package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.responseDto.ChatInboxResponseDto;
import com.web.webhook.entity.WebhookLog;
import com.web.webhook.repository.WebhookLogRepository;
import com.web.webhook.service.ChatInboxService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatInboxServiceImpl
        implements ChatInboxService {

    private final WebhookLogRepository webhookLogRepository;

    public ChatInboxServiceImpl(
            WebhookLogRepository webhookLogRepository) {

        this.webhookLogRepository = webhookLogRepository;
    }

    private ChatInboxResponseDto convertToDto(
            WebhookLog log) {

        ChatInboxResponseDto dto =
                new ChatInboxResponseDto();

        dto.setId(log.getId());
        dto.setMessageId(log.getMessageId());
        dto.setPhoneNumber(log.getPhoneNumber());
        dto.setStatus(log.getStatus());
        dto.setReceivedAt(
                log.getReceivedAt() != null
                        ? log.getReceivedAt().toString()
                        : null
        );

        return dto;
    }

    @Override
    public List<ChatInboxResponseDto> getAllIncomingMessages() {

        // status = "incoming" means message came FROM the customer TO us
        List<WebhookLog> logs =
                webhookLogRepository.findByStatus("incoming");

        List<ChatInboxResponseDto> result = new ArrayList<>();

        for (WebhookLog log : logs) {
            result.add(convertToDto(log));
        }

        System.out.println("[CHAT INBOX] Fetched all incoming messages. Count: "
                + result.size());

        return result;
    }

    @Override
    public List<ChatInboxResponseDto> getMessagesByPhoneNumber(
            String phoneNumber) {

        // fetch all messages (incoming + delivery status) for this phone number
        List<WebhookLog> logs =
                webhookLogRepository.findByPhoneNumber(phoneNumber);

        List<ChatInboxResponseDto> result = new ArrayList<>();

        for (WebhookLog log : logs) {
            result.add(convertToDto(log));
        }

        System.out.println("[CHAT INBOX] Fetched messages for phone: "
                + phoneNumber + " | Count: " + result.size());

        return result;
    }
}