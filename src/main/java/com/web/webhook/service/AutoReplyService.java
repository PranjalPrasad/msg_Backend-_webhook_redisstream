package com.web.webhook.service;

import com.web.webhook.dto.requestDto.AutoReplyRequestDto;
import com.web.webhook.dto.responseDto.AutoReplyResponseDto;

import java.util.List;

public interface AutoReplyService {

    AutoReplyResponseDto createRule(
            AutoReplyRequestDto requestDto
    );

    List<AutoReplyResponseDto> getAllRules();

    AutoReplyResponseDto getRuleById(
            Long id
    );

    AutoReplyResponseDto updateRule(
            Long id,
            AutoReplyRequestDto requestDto
    );

    AutoReplyResponseDto patchRule(
            Long id,
            AutoReplyRequestDto requestDto
    );

    void deleteRule(
            Long id
    );

    // webhook ke andar incoming message ka keyword match karne ke liye
    // MetaApiService configure hone ke baad isse reply bhejega
    String findMatchingReply(
            String incomingMessageText
    );
}