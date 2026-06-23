package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.TemplateRequestDto;
import com.web.webhook.dto.responseDto.TemplateResponseDto;
import com.web.webhook.entity.MessageTemplate;
import com.web.webhook.repository.MessageTemplateRepository;
import com.web.webhook.service.TemplateService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateServiceImpl
        implements TemplateService {

    private final MessageTemplateRepository repository;

    public TemplateServiceImpl(
            MessageTemplateRepository repository) {

        this.repository = repository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private TemplateResponseDto convertToDto(
            MessageTemplate template) {

        TemplateResponseDto dto =
                new TemplateResponseDto();

        dto.setId(template.getId());
        dto.setTemplateName(template.getTemplateName());
        dto.setTemplateType(template.getTemplateType());
        dto.setTemplateBody(template.getTemplateBody());
        dto.setStatus(template.getStatus());

        return dto;
    }

    @Override
    public TemplateResponseDto createTemplate(
            TemplateRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        if(repository
                .existsByTemplateNameAndCreatedBy(
                        requestDto.getTemplateName(),
                        email
                )) {

            throw new RuntimeException(
                    "Template Already Exists"
            );
        }

        MessageTemplate template =
                new MessageTemplate();

        template.setTemplateName(
                requestDto.getTemplateName());

        template.setTemplateType(
                requestDto.getTemplateType());

        template.setTemplateBody(
                requestDto.getTemplateBody());

        template.setStatus(
                requestDto.getStatus());

        template.setCreatedBy(email);

        template.setCreatedAt(
                LocalDateTime.now());

        template.setUpdatedAt(
                LocalDateTime.now());

        MessageTemplate saved =
                repository.save(template);

        return convertToDto(saved);
    }

    @Override
    public List<TemplateResponseDto>
    getAllTemplates() {

        String email =
                getLoggedInUserEmail();

        List<MessageTemplate> templates =
                repository.findByCreatedBy(
                        email);

        List<TemplateResponseDto> result =
                new ArrayList<>();

        for(MessageTemplate template :
                templates){

            result.add(
                    convertToDto(template)
            );
        }

        return result;
    }

    @Override
    public TemplateResponseDto getTemplateById(
            Long id) {

        String email =
                getLoggedInUserEmail();

        MessageTemplate template =
                repository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Template Not Found"
                                )
                        );

        return convertToDto(template);
    }

    @Override
    public TemplateResponseDto updateTemplate(
            Long id,
            TemplateRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        MessageTemplate template =
                repository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Template Not Found"
                                )
                        );

        template.setTemplateName(
                requestDto.getTemplateName());

        template.setTemplateType(
                requestDto.getTemplateType());

        template.setTemplateBody(
                requestDto.getTemplateBody());

        template.setStatus(
                requestDto.getStatus());

        template.setUpdatedAt(
                LocalDateTime.now());

        MessageTemplate updated =
                repository.save(template);

        return convertToDto(updated);
    }

    @Override
    public TemplateResponseDto patchTemplate(
            Long id,
            TemplateRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        MessageTemplate template =
                repository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Template Not Found"
                                )
                        );

        if(requestDto.getTemplateName() != null){
            template.setTemplateName(
                    requestDto.getTemplateName());
        }

        if(requestDto.getTemplateType() != null){
            template.setTemplateType(
                    requestDto.getTemplateType());
        }

        if(requestDto.getTemplateBody() != null){
            template.setTemplateBody(
                    requestDto.getTemplateBody());
        }

        if(requestDto.getStatus() != null){
            template.setStatus(
                    requestDto.getStatus());
        }

        template.setUpdatedAt(
                LocalDateTime.now());

        MessageTemplate updated =
                repository.save(template);

        return convertToDto(updated);
    }

    @Override
    public void deleteTemplate(
            Long id) {

        String email =
                getLoggedInUserEmail();

        MessageTemplate template =
                repository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Template Not Found"
                                )
                        );

        repository.delete(template);
    }
}