package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.ApiKeyRequestDto;
import com.web.webhook.dto.responseDto.ApiKeyResponseDto;
import com.web.webhook.entity.ApiKey;
import com.web.webhook.repository.ApiKeyRepository;
import com.web.webhook.service.ApiKeyService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiKeyServiceImpl
        implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public ApiKeyServiceImpl(
            ApiKeyRepository apiKeyRepository) {

        this.apiKeyRepository = apiKeyRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    // random 40 character key generate karta hai jaise "sk_live_abc123..."
    private String generateRandomKey() {

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("sk_live_");

        for (int i = 0; i < 32; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    private ApiKeyResponseDto convertToDto(
            ApiKey apiKey) {

        ApiKeyResponseDto dto =
                new ApiKeyResponseDto();

        dto.setId(apiKey.getId());
        dto.setKeyName(apiKey.getKeyName());
        dto.setKeyValue(apiKey.getKeyValue());
        dto.setStatus(apiKey.getStatus());
        dto.setCreatedBy(apiKey.getCreatedBy());
        dto.setCreatedAt(
                apiKey.getCreatedAt() != null
                        ? apiKey.getCreatedAt().toString() : null
        );
        dto.setUpdatedAt(
                apiKey.getUpdatedAt() != null
                        ? apiKey.getUpdatedAt().toString() : null
        );

        return dto;
    }

    @Override
    public ApiKeyResponseDto generateKey(
            ApiKeyRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        // unique key generate karo — agar by chance duplicate ban jaye toh dobara try karo
        String generatedKey = generateRandomKey();

        while (apiKeyRepository.existsByKeyValue(generatedKey)) {
            generatedKey = generateRandomKey();
        }

        ApiKey apiKey = new ApiKey();
        apiKey.setKeyName(requestDto.getKeyName());
        apiKey.setKeyValue(generatedKey);
        apiKey.setStatus("ACTIVE");
        apiKey.setCreatedBy(email);
        apiKey.setCreatedAt(LocalDateTime.now());
        apiKey.setUpdatedAt(LocalDateTime.now());

        ApiKey saved = apiKeyRepository.save(apiKey);

        System.out.println("[API KEY] New key generated: " + saved.getKeyName()
                + " | by: " + email);

        return convertToDto(saved);
    }

    @Override
    public List<ApiKeyResponseDto> getAllKeys() {

        String email = getLoggedInUserEmail();

        List<ApiKey> keys =
                apiKeyRepository.findByCreatedBy(email);

        List<ApiKeyResponseDto> result = new ArrayList<>();

        for (ApiKey apiKey : keys) {
            result.add(convertToDto(apiKey));
        }

        return result;
    }

    @Override
    public ApiKeyResponseDto getKeyById(Long id) {

        String email = getLoggedInUserEmail();

        ApiKey apiKey = apiKeyRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "API key not found with id: " + id
                        ));

        return convertToDto(apiKey);
    }

    @Override
    public ApiKeyResponseDto updateKeyStatus(
            Long id,
            ApiKeyRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        ApiKey apiKey = apiKeyRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "API key not found with id: " + id
                        ));

        if (requestDto.getKeyName() != null) {
            apiKey.setKeyName(requestDto.getKeyName());
        }

        if (requestDto.getStatus() != null) {
            apiKey.setStatus(requestDto.getStatus());
        }

        apiKey.setUpdatedAt(LocalDateTime.now());

        ApiKey updated = apiKeyRepository.save(apiKey);

        System.out.println("[API KEY] Key status updated: " + updated.getId()
                + " | New status: " + updated.getStatus());

        return convertToDto(updated);
    }

    @Override
    public void revokeKey(Long id) {

        String email = getLoggedInUserEmail();

        ApiKey apiKey = apiKeyRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "API key not found with id: " + id
                        ));

        apiKeyRepository.delete(apiKey);

        System.out.println("[API KEY] Key revoked/deleted: " + id);
    }

    @Override
    public boolean isValidKey(String keyValue) {

        return apiKeyRepository.findByKeyValue(keyValue)
                .map(apiKey -> "ACTIVE".equalsIgnoreCase(apiKey.getStatus()))
                .orElse(false);
    }
}