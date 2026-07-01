package com.web.webhook.service;

import com.web.webhook.dto.responseDto.ChatInboxResponseDto;

import java.util.List;

public interface ChatInboxService {

    // all incoming messages
    List<ChatInboxResponseDto> getAllIncomingMessages();

    // conversation with specific phone number
    List<ChatInboxResponseDto> getMessagesByPhoneNumber(
            String phoneNumber
    );
}