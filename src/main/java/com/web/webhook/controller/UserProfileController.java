package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.ChangePasswordRequestDto;
import com.web.webhook.dto.requestDto.UserProfileRequestDto;
import com.web.webhook.dto.responseDto.UserProfileResponseDto;
import com.web.webhook.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(
            UserProfileService userProfileService) {

        this.userProfileService = userProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDto>
    getProfile() {

        return ResponseEntity.ok(
                userProfileService.getProfile()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponseDto>
    updateProfile(
            @RequestBody
            UserProfileRequestDto requestDto) {

        return ResponseEntity.ok(
                userProfileService.updateProfile(
                        requestDto
                )
        );
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResponseDto>
    patchProfile(
            @RequestBody
            UserProfileRequestDto requestDto) {

        return ResponseEntity.ok(
                userProfileService.patchProfile(
                        requestDto
                )
        );
    }

    @PatchMapping("/change-password")
    public ResponseEntity<UserProfileResponseDto>
    changePassword(
            @RequestBody
            ChangePasswordRequestDto requestDto) {

        return ResponseEntity.ok(
                userProfileService.changePassword(
                        requestDto
                )
        );
    }
}