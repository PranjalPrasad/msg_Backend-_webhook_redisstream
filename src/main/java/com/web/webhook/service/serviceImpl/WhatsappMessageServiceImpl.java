package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.WhatsappMessageRequestDto;
import com.web.webhook.dto.responseDto.WhatsappMessageResponseDto;
import com.web.webhook.entity.Contact;
import com.web.webhook.entity.MessageTemplate;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.ContactRepository;
import com.web.webhook.repository.MessageTemplateRepository;
import com.web.webhook.repository.WhatsappMessageRepository;
import com.web.webhook.service.RedisProducerService;
import com.web.webhook.service.WhatsappMessageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WhatsappMessageServiceImpl
        implements WhatsappMessageService {

    private final WhatsappMessageRepository messageRepository;
    private final ContactRepository contactRepository;
    private final MessageTemplateRepository templateRepository;
    private final RedisProducerService redisProducerService;

    public WhatsappMessageServiceImpl(
            WhatsappMessageRepository messageRepository,
            ContactRepository contactRepository,
            MessageTemplateRepository templateRepository,
            RedisProducerService redisProducerService) {

        this.messageRepository = messageRepository;
        this.contactRepository = contactRepository;
        this.templateRepository = templateRepository;
        this.redisProducerService = redisProducerService;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private WhatsappMessageResponseDto convertToDto(
            WhatsappMessage message) {

        WhatsappMessageResponseDto dto =
                new WhatsappMessageResponseDto();

        dto.setId(message.getId());
        dto.setContactId(message.getContactId());
        dto.setTemplateId(message.getTemplateId());
        dto.setPhoneNumber(message.getPhoneNumber());
        dto.setMessageBody(message.getMessageBody());
        dto.setStatus(message.getStatus());

        return dto;
    }

    @Override
    public WhatsappMessageResponseDto createMessage(
            WhatsappMessageRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        Contact contact =
                contactRepository
                        .findByIdAndCreatedBy(
                                requestDto.getContactId(),
                                email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contact Not Found")
                        );

        MessageTemplate template =
                templateRepository
                        .findByIdAndCreatedBy(
                                requestDto.getTemplateId(),
                                email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Template Not Found")
                        );

        WhatsappMessage message =
                new WhatsappMessage();

        message.setContactId(contact.getId());

        message.setTemplateId(template.getId());

        message.setPhoneNumber(
                contact.getPhoneNumber()
        );

        message.setMessageBody(
                template.getTemplateBody()
        );

        message.setStatus("QUEUED");

        message.setCreatedBy(email);

        message.setCreatedAt(
                LocalDateTime.now()
        );

        message.setUpdatedAt(
                LocalDateTime.now()
        );

        WhatsappMessage saved =
                messageRepository.save(message);

        redisProducerService.publishWebhook(
                "MESSAGE_ID=" + saved.getId()
        );

        return convertToDto(saved);
    }

    @Override
    public List<WhatsappMessageResponseDto>
    getAllMessages() {

        String email =
                getLoggedInUserEmail();

        List<WhatsappMessage> messages =
                messageRepository.findByCreatedBy(
                        email
                );

        List<WhatsappMessageResponseDto> result =
                new ArrayList<>();

        for(WhatsappMessage message : messages){

            result.add(
                    convertToDto(message)
            );
        }

        return result;
    }

    @Override
    public WhatsappMessageResponseDto
    getMessageById(Long id) {

        String email =
                getLoggedInUserEmail();

        WhatsappMessage message =
                messageRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message Not Found"
                                )
                        );

        return convertToDto(message);
    }

    @Override
    public WhatsappMessageResponseDto
    updateMessage(
            Long id,
            WhatsappMessageRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        WhatsappMessage message =
                messageRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message Not Found"
                                )
                        );

        Contact contact =
                contactRepository
                        .findByIdAndCreatedBy(
                                requestDto.getContactId(),
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contact Not Found"
                                )
                        );

        MessageTemplate template =
                templateRepository
                        .findByIdAndCreatedBy(
                                requestDto.getTemplateId(),
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Template Not Found"
                                )
                        );

        message.setContactId(contact.getId());

        message.setTemplateId(template.getId());

        message.setPhoneNumber(
                contact.getPhoneNumber()
        );

        message.setMessageBody(
                template.getTemplateBody()
        );

        message.setUpdatedAt(
                LocalDateTime.now()
        );

        WhatsappMessage updated =
                messageRepository.save(message);

        return convertToDto(updated);
    }

    @Override
    public WhatsappMessageResponseDto
    patchMessage(
            Long id,
            WhatsappMessageRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        WhatsappMessage message =
                messageRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message Not Found"
                                )
                        );

        if(requestDto.getContactId() != null){

            Contact contact =
                    contactRepository
                            .findByIdAndCreatedBy(
                                    requestDto.getContactId(),
                                    email
                            )
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Contact Not Found"
                                    )
                            );

            message.setContactId(contact.getId());
            message.setPhoneNumber(
                    contact.getPhoneNumber()
            );
        }

        if(requestDto.getTemplateId() != null){

            MessageTemplate template =
                    templateRepository
                            .findByIdAndCreatedBy(
                                    requestDto.getTemplateId(),
                                    email
                            )
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Template Not Found"
                                    )
                            );

            message.setTemplateId(
                    template.getId()
            );

            message.setMessageBody(
                    template.getTemplateBody()
            );
        }

        message.setUpdatedAt(
                LocalDateTime.now()
        );

        WhatsappMessage updated =
                messageRepository.save(message);

        return convertToDto(updated);
    }

    @Override
    public void deleteMessage(Long id) {

        String email =
                getLoggedInUserEmail();

        WhatsappMessage message =
                messageRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Message Not Found"
                                )
                        );

        messageRepository.delete(message);
    }
}