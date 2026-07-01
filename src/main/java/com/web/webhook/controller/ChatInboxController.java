package com.web.webhook.controller;

import com.web.webhook.dto.responseDto.ChatInboxResponseDto;
import com.web.webhook.service.ChatInboxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inbox")
public class ChatInboxController {

    private final ChatInboxService chatInboxService;

    public ChatInboxController(
            ChatInboxService chatInboxService) {

        this.chatInboxService = chatInboxService;
    }

    // GET all incoming messages from customers
    @GetMapping("/messages")
    public ResponseEntity<List<ChatInboxResponseDto>>
    getAllIncomingMessages() {

        return ResponseEntity.ok(
                chatInboxService.getAllIncomingMessages()
        );
    }

    // GET conversation with a specific phone number
    @GetMapping("/messages/{phoneNumber}")
    public ResponseEntity<List<ChatInboxResponseDto>>
    getMessagesByPhoneNumber(
            @PathVariable String phoneNumber) {

        return ResponseEntity.ok(
                chatInboxService.getMessagesByPhoneNumber(
                        phoneNumber
                )
        );
    }
}