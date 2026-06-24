package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.MessageStatusRequestDto;
import com.web.webhook.dto.responseDto.MessageStatusResponseDto;
import com.web.webhook.service.MessageStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message-status")
public class MessageStatusController {

    private final MessageStatusService service;

    public MessageStatusController(
            MessageStatusService service) {

        this.service = service;
    }

    @PatchMapping("/update-status/{messageId}")
    public ResponseEntity<MessageStatusResponseDto>
    updateStatus(
            @PathVariable Long messageId,
            @RequestBody MessageStatusRequestDto requestDto){

        return ResponseEntity.ok(
                service.updateStatus(
                        messageId,
                        requestDto
                )
        );
    }
}