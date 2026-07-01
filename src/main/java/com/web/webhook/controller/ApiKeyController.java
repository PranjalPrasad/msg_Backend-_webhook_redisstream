package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.ApiKeyRequestDto;
import com.web.webhook.dto.responseDto.ApiKeyResponseDto;
import com.web.webhook.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(
            ApiKeyService apiKeyService) {

        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiKeyResponseDto>
    generateKey(
            @RequestBody
            ApiKeyRequestDto requestDto) {

        return ResponseEntity.ok(
                apiKeyService.generateKey(requestDto)
        );
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponseDto>>
    getAllKeys() {

        return ResponseEntity.ok(
                apiKeyService.getAllKeys()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyResponseDto>
    getKeyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                apiKeyService.getKeyById(id)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiKeyResponseDto>
    updateKeyStatus(
            @PathVariable Long id,
            @RequestBody
            ApiKeyRequestDto requestDto) {

        return ResponseEntity.ok(
                apiKeyService.updateKeyStatus(id, requestDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    revokeKey(
            @PathVariable Long id) {

        apiKeyService.revokeKey(id);

        return ResponseEntity.ok(
                "API key revoked successfully"
        );
    }
}