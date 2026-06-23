package com.web.webhook.service;

import com.web.webhook.dto.requestDto.TemplateRequestDto;
import com.web.webhook.dto.responseDto.TemplateResponseDto;

import java.util.List;

public interface TemplateService {

    TemplateResponseDto createTemplate(
            TemplateRequestDto requestDto
    );

    List<TemplateResponseDto> getAllTemplates();

    TemplateResponseDto getTemplateById(
            Long id
    );

    TemplateResponseDto updateTemplate(
            Long id,
            TemplateRequestDto requestDto
    );

    TemplateResponseDto patchTemplate(
            Long id,
            TemplateRequestDto requestDto
    );

    void deleteTemplate(
            Long id
    );
}