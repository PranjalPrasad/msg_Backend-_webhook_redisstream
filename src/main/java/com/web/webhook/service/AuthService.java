package com.web.webhook.service;

import com.web.webhook.dto.requestDto.LoginRequestDto;
import com.web.webhook.dto.requestDto.RegisterRequestDto;
import com.web.webhook.dto.responseDto.AuthResponseDto;

public interface AuthService {

    AuthResponseDto register(
            RegisterRequestDto request);

    AuthResponseDto login(
            LoginRequestDto request);
}