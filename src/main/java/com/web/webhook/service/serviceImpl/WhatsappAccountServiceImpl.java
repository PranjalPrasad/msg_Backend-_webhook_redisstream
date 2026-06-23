package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.WhatsappAccountRequestDto;
import com.web.webhook.dto.responseDto.WhatsappAccountResponseDto;
import com.web.webhook.entity.WhatsappAccount;
import com.web.webhook.repository.WhatsappAccountRepository;
import com.web.webhook.service.WhatsappAccountService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WhatsappAccountServiceImpl
        implements WhatsappAccountService {

    private final WhatsappAccountRepository whatsappAccountRepository;

    public WhatsappAccountServiceImpl(
            WhatsappAccountRepository whatsappAccountRepository) {

        this.whatsappAccountRepository =
                whatsappAccountRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private WhatsappAccountResponseDto convertToDto(
            WhatsappAccount account) {

        WhatsappAccountResponseDto dto =
                new WhatsappAccountResponseDto();

        dto.setId(account.getId());

        dto.setBusinessAccountId(
                account.getBusinessAccountId());

        dto.setPhoneNumberId(
                account.getPhoneNumberId());

        dto.setVerifyToken(
                account.getVerifyToken());

        dto.setWebhookUrl(
                account.getWebhookUrl());

        dto.setStatus(
                account.getStatus());

        return dto;
    }

    @Override
    public WhatsappAccountResponseDto createAccount(
            WhatsappAccountRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        if(whatsappAccountRepository
                .existsByPhoneNumberIdAndCreatedBy(
                        requestDto.getPhoneNumberId(),
                        email
                )) {

            throw new RuntimeException(
                    "WhatsApp Account Already Exists"
            );
        }

        WhatsappAccount account =
                new WhatsappAccount();

        account.setBusinessAccountId(
                requestDto.getBusinessAccountId());

        account.setPhoneNumberId(
                requestDto.getPhoneNumberId());

        account.setAccessToken(
                requestDto.getAccessToken());

        account.setVerifyToken(
                requestDto.getVerifyToken());

        account.setWebhookUrl(
                requestDto.getWebhookUrl());

        account.setStatus(
                requestDto.getStatus());

        account.setCreatedBy(email);

        account.setCreatedAt(
                LocalDateTime.now());

        account.setUpdatedAt(
                LocalDateTime.now());

        WhatsappAccount saved =
                whatsappAccountRepository.save(account);

        return convertToDto(saved);
    }

    @Override
    public List<WhatsappAccountResponseDto>
    getAllAccounts() {

        String email =
                getLoggedInUserEmail();

        List<WhatsappAccount> accounts =
                whatsappAccountRepository
                        .findByCreatedBy(email);

        List<WhatsappAccountResponseDto> result =
                new ArrayList<>();

        for(WhatsappAccount account : accounts){

            result.add(
                    convertToDto(account)
            );
        }

        return result;
    }

    @Override
    public WhatsappAccountResponseDto
    getAccountById(Long id) {

        String email =
                getLoggedInUserEmail();

        WhatsappAccount account =
                whatsappAccountRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Account Not Found"
                                )
                        );

        return convertToDto(account);
    }

    @Override
    public WhatsappAccountResponseDto updateAccount(
            Long id,
            WhatsappAccountRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        WhatsappAccount account =
                whatsappAccountRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Account Not Found"
                                )
                        );

        account.setBusinessAccountId(
                requestDto.getBusinessAccountId());

        account.setPhoneNumberId(
                requestDto.getPhoneNumberId());

        account.setAccessToken(
                requestDto.getAccessToken());

        account.setVerifyToken(
                requestDto.getVerifyToken());

        account.setWebhookUrl(
                requestDto.getWebhookUrl());

        account.setStatus(
                requestDto.getStatus());

        account.setUpdatedAt(
                LocalDateTime.now());

        WhatsappAccount updated =
                whatsappAccountRepository
                        .save(account);

        return convertToDto(updated);
    }

    @Override
    public WhatsappAccountResponseDto patchAccount(
            Long id,
            WhatsappAccountRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        WhatsappAccount account =
                whatsappAccountRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Account Not Found"
                                )
                        );

        if(requestDto.getBusinessAccountId() != null){

            account.setBusinessAccountId(
                    requestDto.getBusinessAccountId());
        }

        if(requestDto.getPhoneNumberId() != null){

            account.setPhoneNumberId(
                    requestDto.getPhoneNumberId());
        }

        if(requestDto.getAccessToken() != null){

            account.setAccessToken(
                    requestDto.getAccessToken());
        }

        if(requestDto.getVerifyToken() != null){

            account.setVerifyToken(
                    requestDto.getVerifyToken());
        }

        if(requestDto.getWebhookUrl() != null){

            account.setWebhookUrl(
                    requestDto.getWebhookUrl());
        }

        if(requestDto.getStatus() != null){

            account.setStatus(
                    requestDto.getStatus());
        }

        account.setUpdatedAt(
                LocalDateTime.now());

        WhatsappAccount updated =
                whatsappAccountRepository
                        .save(account);

        return convertToDto(updated);
    }

    @Override
    public void deleteAccount(
            Long id) {

        String email =
                getLoggedInUserEmail();

        WhatsappAccount account =
                whatsappAccountRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Account Not Found"
                                )
                        );

        whatsappAccountRepository
                .delete(account);
    }
}
