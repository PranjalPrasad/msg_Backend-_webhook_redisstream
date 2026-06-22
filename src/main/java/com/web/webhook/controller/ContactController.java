package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.ContactRequestDto;
import com.web.webhook.dto.responseDto.ContactResponseDto;
import com.web.webhook.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(
            ContactService contactService) {

        this.contactService = contactService;
    }

    @PostMapping("/create-contact")
    public ResponseEntity<ContactResponseDto> createContact(
            @RequestBody ContactRequestDto requestDto) {

        return ResponseEntity.ok(
                contactService.createContact(
                        requestDto
                )
        );
    }

    @GetMapping("/get-all-contacts")
    public ResponseEntity<List<ContactResponseDto>> getAllContacts() {

        return ResponseEntity.ok(
                contactService.getAllContacts()
        );
    }

    @PatchMapping("/patch-contact/{id}")
    public ResponseEntity<ContactResponseDto> patchContact(
            @PathVariable Long id,
            @RequestBody ContactRequestDto requestDto) {

        return ResponseEntity.ok(
                contactService.patchContact(
                        id,
                        requestDto
                )
        );
    }
    @GetMapping("/get-contact-by-id/{id}")
    public ResponseEntity<ContactResponseDto> getContactById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                contactService.getContactById(id)
        );
    }

    @PutMapping("/update-contact/{id}")
    public ResponseEntity<ContactResponseDto> updateContact(
            @PathVariable Long id,
            @RequestBody ContactRequestDto requestDto) {

        return ResponseEntity.ok(
                contactService.updateContact(
                        id,
                        requestDto
                )
        );
    }

    @DeleteMapping("/delete-contact/{id}")
    public ResponseEntity<String> deleteContact(
            @PathVariable Long id) {

        contactService.deleteContact(id);

        return ResponseEntity.ok(
                "Contact Deleted Successfully"
        );
    }
}
