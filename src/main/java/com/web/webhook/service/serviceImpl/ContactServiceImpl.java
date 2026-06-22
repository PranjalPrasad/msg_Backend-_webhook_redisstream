package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.ContactRequestDto;
import com.web.webhook.dto.responseDto.ContactResponseDto;
import com.web.webhook.entity.Contact;
import com.web.webhook.repository.ContactRepository;
import com.web.webhook.service.ContactService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl
        implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(
            ContactRepository contactRepository) {

        this.contactRepository = contactRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @Override
    public ContactResponseDto patchContact(
            Long id,
            ContactRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        Contact contact =
                contactRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contact Not Found"
                                )
                        );

        if(requestDto.getName() != null){
            contact.setName(
                    requestDto.getName()
            );
        }

        if(requestDto.getPhoneNumber() != null){
            contact.setPhoneNumber(
                    requestDto.getPhoneNumber()
            );
        }

        if(requestDto.getEmail() != null){
            contact.setEmail(
                    requestDto.getEmail()
            );
        }

        if(requestDto.getCity() != null){
            contact.setCity(
                    requestDto.getCity()
            );
        }

        if(requestDto.getStatus() != null){
            contact.setStatus(
                    requestDto.getStatus()
            );
        }

        contact.setUpdatedAt(
                java.time.LocalDateTime.now()
        );

        Contact updated =
                contactRepository.save(contact);

        return convertToDto(updated);
    }

    @Override
    public ContactResponseDto createContact(
            ContactRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();




        if(contactRepository
                .existsByPhoneNumberAndCreatedBy(
                        requestDto.getPhoneNumber(),
                        email
                )) {

            throw new RuntimeException(
                    "Phone Number Already Exists"
            );
        }
        Contact contact =
                new Contact();


        contact.setName(
                requestDto.getName());

        contact.setPhoneNumber(
                requestDto.getPhoneNumber());

        contact.setEmail(
                requestDto.getEmail());

        contact.setCity(
                requestDto.getCity());

        contact.setStatus(
                requestDto.getStatus());

        contact.setCreatedBy(email);

        contact.setCreatedAt(
                LocalDateTime.now());

        contact.setUpdatedAt(
                LocalDateTime.now());

        Contact saved =
                contactRepository.save(contact);

        return convertToDto(saved);
    }

    @Override
    public List<ContactResponseDto> getAllContacts() {

        String email =
                getLoggedInUserEmail();

        System.out.println(
                "Logged In Email = " + email
        );

        List<Contact> contacts =
                contactRepository.findByCreatedBy(
                        email);

        List<ContactResponseDto> result =
                new ArrayList<>();

        for(Contact contact : contacts){

            result.add(
                    convertToDto(contact)
            );
        }

        return result;
    }

    @Override
    public ContactResponseDto getContactById(
            Long id) {

        String email =
                getLoggedInUserEmail();

        Contact contact =
                contactRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contact Not Found")
                        );

        return convertToDto(contact);
    }

    @Override
    public ContactResponseDto updateContact(
            Long id,
            ContactRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        Contact contact =
                contactRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contact Not Found")
                        );

        contact.setName(
                requestDto.getName());

        contact.setPhoneNumber(
                requestDto.getPhoneNumber());

        contact.setEmail(
                requestDto.getEmail());

        contact.setCity(
                requestDto.getCity());

        contact.setStatus(
                requestDto.getStatus());

        contact.setUpdatedAt(
                LocalDateTime.now());

        Contact updated =
                contactRepository.save(contact);

        return convertToDto(updated);
    }

    @Override
    public void deleteContact(
            Long id) {

        String email =
                getLoggedInUserEmail();

        Contact contact =
                contactRepository
                        .findByIdAndCreatedBy(
                                id,
                                email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Contact Not Found")
                        );

        contactRepository.delete(contact);
    }

    private ContactResponseDto convertToDto(
            Contact contact) {

        ContactResponseDto dto =
                new ContactResponseDto();

        dto.setId(contact.getId());

        dto.setName(contact.getName());

        dto.setPhoneNumber(
                contact.getPhoneNumber());

        dto.setEmail(
                contact.getEmail());

        dto.setCity(
                contact.getCity());

        dto.setStatus(
                contact.getStatus());

        return dto;
    }
}
