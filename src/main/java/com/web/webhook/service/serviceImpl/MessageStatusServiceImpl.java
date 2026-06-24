package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.MessageStatusRequestDto;
import com.web.webhook.dto.responseDto.MessageStatusResponseDto;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.MessageStatusService;
import org.springframework.stereotype.Service;

@Service
public class MessageStatusServiceImpl
        implements MessageStatusService {

    private final WhatsappMessageRepository repository;

    public MessageStatusServiceImpl(
            WhatsappMessageRepository repository) {

        this.repository = repository;
    }

    @Override
    public MessageStatusResponseDto updateStatus(
            Long messageId,
            MessageStatusRequestDto requestDto) {

        WhatsappMessage message =
                repository.findById(messageId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message Not Found"
                                )
                        );

        String oldStatus =
                message.getStatus();

        message.setStatus(
                requestDto.getStatus()
        );

        repository.save(message);

        MessageStatusResponseDto response =
                new MessageStatusResponseDto();

        response.setMessageId(
                message.getId()
        );

        response.setOldStatus(
                oldStatus
        );

        response.setNewStatus(
                requestDto.getStatus()
        );

        return response;
    }
}