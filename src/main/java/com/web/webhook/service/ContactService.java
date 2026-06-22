package com.web.webhook.service;

import com.web.webhook.dto.requestDto.ContactRequestDto;
import com.web.webhook.dto.responseDto.ContactResponseDto;

import java.util.List;

public interface ContactService {

    ContactResponseDto createContact(
            ContactRequestDto requestDto
    );

    List<ContactResponseDto> getAllContacts();

    ContactResponseDto getContactById(
            Long id
    );

    ContactResponseDto updateContact(
            Long id,
            ContactRequestDto requestDto
    );

    void deleteContact(
            Long id
    );

    ContactResponseDto patchContact(
            Long id,
            ContactRequestDto requestDto
    );
}
