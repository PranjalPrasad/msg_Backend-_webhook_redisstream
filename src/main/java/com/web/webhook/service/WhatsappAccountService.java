package com.web.webhook.service;

import com.web.webhook.dto.requestDto.WhatsappAccountRequestDto;
import com.web.webhook.dto.responseDto.WhatsappAccountResponseDto;

import java.util.List;

public interface WhatsappAccountService {

    WhatsappAccountResponseDto createAccount(
            WhatsappAccountRequestDto requestDto);

    List<WhatsappAccountResponseDto> getAllAccounts();

    WhatsappAccountResponseDto getAccountById(
            Long id);

    WhatsappAccountResponseDto updateAccount(
            Long id,
            WhatsappAccountRequestDto requestDto);

    WhatsappAccountResponseDto patchAccount(
            Long id,
            WhatsappAccountRequestDto requestDto);

    void deleteAccount(
            Long id);
}