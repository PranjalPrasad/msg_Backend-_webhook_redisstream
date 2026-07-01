package com.web.webhook.service;


import com.web.webhook.dto.requestDto.ChangePasswordRequestDto;
import com.web.webhook.dto.requestDto.UserProfileRequestDto;
import com.web.webhook.dto.responseDto.UserProfileResponseDto;

public interface UserProfileService {

    UserProfileResponseDto getProfile();

    UserProfileResponseDto updateProfile(
            UserProfileRequestDto requestDto
    );

    UserProfileResponseDto patchProfile(
            UserProfileRequestDto requestDto
    );

    UserProfileResponseDto changePassword(
            ChangePasswordRequestDto requestDto
    );
}