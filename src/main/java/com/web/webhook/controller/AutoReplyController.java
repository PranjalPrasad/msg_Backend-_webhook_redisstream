package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.AutoReplyRequestDto;
import com.web.webhook.dto.responseDto.AutoReplyResponseDto;
import com.web.webhook.service.AutoReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auto-reply")
public class AutoReplyController {

    private final AutoReplyService autoReplyService;

    public AutoReplyController(
            AutoReplyService autoReplyService) {

        this.autoReplyService = autoReplyService;
    }

    @PostMapping("/rules")
    public ResponseEntity<AutoReplyResponseDto>
    createRule(
            @RequestBody
            AutoReplyRequestDto requestDto) {

        return ResponseEntity.ok(
                autoReplyService.createRule(requestDto)
        );
    }

    @GetMapping("/rules")
    public ResponseEntity<List<AutoReplyResponseDto>>
    getAllRules() {

        return ResponseEntity.ok(
                autoReplyService.getAllRules()
        );
    }

    @GetMapping("/rules/{id}")
    public ResponseEntity<AutoReplyResponseDto>
    getRuleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                autoReplyService.getRuleById(id)
        );
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<AutoReplyResponseDto>
    updateRule(
            @PathVariable Long id,
            @RequestBody
            AutoReplyRequestDto requestDto) {

        return ResponseEntity.ok(
                autoReplyService.updateRule(id, requestDto)
        );
    }

    @PatchMapping("/rules/{id}")
    public ResponseEntity<AutoReplyResponseDto>
    patchRule(
            @PathVariable Long id,
            @RequestBody
            AutoReplyRequestDto requestDto) {

        return ResponseEntity.ok(
                autoReplyService.patchRule(id, requestDto)
        );
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<String>
    deleteRule(
            @PathVariable Long id) {

        autoReplyService.deleteRule(id);

        return ResponseEntity.ok(
                "Auto reply rule deleted successfully"
        );
    }
}