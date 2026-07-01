package com.web.webhook.service;

import com.web.webhook.dto.requestDto.ApiKeyRequestDto;
import com.web.webhook.dto.responseDto.ApiKeyResponseDto;

import java.util.List;

public interface ApiKeyService {

    ApiKeyResponseDto generateKey(
            ApiKeyRequestDto requestDto
    );

    List<ApiKeyResponseDto> getAllKeys();

    ApiKeyResponseDto getKeyById(
            Long id
    );

    ApiKeyResponseDto updateKeyStatus(
            Long id,
            ApiKeyRequestDto requestDto
    );

    void revokeKey(
            Long id
    );

    // future: external API requests is method se authenticate honge
    boolean isValidKey(
            String keyValue
    );
}