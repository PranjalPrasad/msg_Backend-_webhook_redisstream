package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.WhatsappAccountRequestDto;
import com.web.webhook.dto.responseDto.WhatsappAccountResponseDto;
import com.web.webhook.service.WhatsappAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/whatsapp")
public class WhatsappAccountController {

    private final WhatsappAccountService whatsappAccountService;

    public WhatsappAccountController(
            WhatsappAccountService whatsappAccountService) {

        this.whatsappAccountService =
                whatsappAccountService;
    }

    @PostMapping("/create-account")
    public ResponseEntity<WhatsappAccountResponseDto>
    createAccount(
            @RequestBody WhatsappAccountRequestDto requestDto){

        return ResponseEntity.ok(
                whatsappAccountService
                        .createAccount(requestDto));
    }

    @GetMapping("/get-all-accounts")
    public ResponseEntity<List<WhatsappAccountResponseDto>>
    getAllAccounts(){

        return ResponseEntity.ok(
                whatsappAccountService
                        .getAllAccounts());
    }

    @GetMapping("/get-account-by-id/{id}")
    public ResponseEntity<WhatsappAccountResponseDto>
    getAccountById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                whatsappAccountService
                        .getAccountById(id));
    }

    @PutMapping("/update-account/{id}")
    public ResponseEntity<WhatsappAccountResponseDto>
    updateAccount(
            @PathVariable Long id,
            @RequestBody WhatsappAccountRequestDto requestDto){

        return ResponseEntity.ok(
                whatsappAccountService
                        .updateAccount(
                                id,
                                requestDto));
    }

    @PatchMapping("/patch-account/{id}")
    public ResponseEntity<WhatsappAccountResponseDto>
    patchAccount(
            @PathVariable Long id,
            @RequestBody WhatsappAccountRequestDto requestDto){

        return ResponseEntity.ok(
                whatsappAccountService
                        .patchAccount(
                                id,
                                requestDto));
    }

    @DeleteMapping("/delete-account/{id}")
    public ResponseEntity<String>
    deleteAccount(
            @PathVariable Long id){

        whatsappAccountService
                .deleteAccount(id);

        return ResponseEntity.ok(
                "WhatsApp Account Deleted Successfully");
    }
}