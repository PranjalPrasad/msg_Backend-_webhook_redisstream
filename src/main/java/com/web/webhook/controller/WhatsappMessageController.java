package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.WhatsappMessageRequestDto;
import com.web.webhook.dto.responseDto.WhatsappMessageResponseDto;
import com.web.webhook.service.WhatsappMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class WhatsappMessageController {

    private final WhatsappMessageService service;

    public WhatsappMessageController(
            WhatsappMessageService service) {

        this.service = service;
    }

    @PostMapping("/create-message")
    public ResponseEntity<WhatsappMessageResponseDto>
    createMessage(
            @RequestBody
            WhatsappMessageRequestDto requestDto){

        return ResponseEntity.ok(
                service.createMessage(
                        requestDto
                )
        );
    }

    @GetMapping("/get-all-messages")
    public ResponseEntity<List<WhatsappMessageResponseDto>>
    getAllMessages(){

        return ResponseEntity.ok(
                service.getAllMessages()
        );
    }

    @GetMapping("/get-message-by-id/{id}")
    public ResponseEntity<WhatsappMessageResponseDto>
    getMessageById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                service.getMessageById(id)
        );
    }

    @PutMapping("/update-message/{id}")
    public ResponseEntity<WhatsappMessageResponseDto>
    updateMessage(
            @PathVariable Long id,
            @RequestBody
            WhatsappMessageRequestDto requestDto){

        return ResponseEntity.ok(
                service.updateMessage(
                        id,
                        requestDto
                )
        );
    }

    @PatchMapping("/patch-message/{id}")
    public ResponseEntity<WhatsappMessageResponseDto>
    patchMessage(
            @PathVariable Long id,
            @RequestBody
            WhatsappMessageRequestDto requestDto){

        return ResponseEntity.ok(
                service.patchMessage(
                        id,
                        requestDto
                )
        );
    }

    @DeleteMapping("/delete-message/{id}")
    public ResponseEntity<String>
    deleteMessage(
            @PathVariable Long id){

        service.deleteMessage(id);

        return ResponseEntity.ok(
                "Message Deleted Successfully"
        );
    }
}