package com.web.webhook.service;

import com.web.webhook.dto.requestDto.WhatsappMessageRequestDto;
import com.web.webhook.dto.responseDto.WhatsappMessageResponseDto;

import java.util.List;

public interface WhatsappMessageService {

    WhatsappMessageResponseDto createMessage(
            WhatsappMessageRequestDto requestDto
    );

    List<WhatsappMessageResponseDto> getAllMessages();

    WhatsappMessageResponseDto getMessageById(
            Long id
    );

    WhatsappMessageResponseDto updateMessage(
            Long id,
            WhatsappMessageRequestDto requestDto
    );

    WhatsappMessageResponseDto patchMessage(
            Long id,
            WhatsappMessageRequestDto requestDto
    );

    void deleteMessage(
            Long id
    );
}
