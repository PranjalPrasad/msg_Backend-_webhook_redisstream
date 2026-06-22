package com.web.webhook.controller;
import com.web.webhook.dto.requestDto.LoginRequestDto;
import com.web.webhook.dto.requestDto.RegisterRequestDto;
import com.web.webhook.dto.responseDto.AuthResponseDto;
import com.web.webhook.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto>
    register(
            @RequestBody
            RegisterRequestDto request) {

        return ResponseEntity.ok(
                authService.register(
                        request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto>
    login(
            @RequestBody
            LoginRequestDto request) {

        return ResponseEntity.ok(
                authService.login(
                        request));
    }

}
