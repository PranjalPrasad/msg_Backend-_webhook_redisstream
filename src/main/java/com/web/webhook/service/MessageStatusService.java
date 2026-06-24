package com.web.webhook.service;

import com.web.webhook.dto.requestDto.MessageStatusRequestDto;
import com.web.webhook.dto.responseDto.MessageStatusResponseDto;

public interface MessageStatusService {

    MessageStatusResponseDto updateStatus(
            Long messageId,
            MessageStatusRequestDto requestDto
    );
}