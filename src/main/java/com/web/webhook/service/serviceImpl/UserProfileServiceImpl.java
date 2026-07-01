package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.ChangePasswordRequestDto;
import com.web.webhook.dto.requestDto.UserProfileRequestDto;
import com.web.webhook.dto.responseDto.UserProfileResponseDto;
import com.web.webhook.entity.User;
import com.web.webhook.repository.UserRepository;
import com.web.webhook.service.UserProfileService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl
        implements UserProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserProfileServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private UserProfileResponseDto convertToDto(
            User user) {

        UserProfileResponseDto dto =
                new UserProfileResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() != null
                ? user.getRole().name() : null);
        dto.setCreatedAt(user.getCreatedAt() != null
                ? user.getCreatedAt().toString() : null);

        return dto;
    }

    @Override
    public UserProfileResponseDto getProfile() {

        String email = getLoggedInUserEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email
                        ));

        return convertToDto(user);
    }

    @Override
    public UserProfileResponseDto updateProfile(
            UserProfileRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email
                        ));

        user.setName(requestDto.getName());

        userRepository.save(user);

        System.out.println("[USER PROFILE] Profile updated for: " + email);

        UserProfileResponseDto dto = convertToDto(user);
        dto.setMessage("Profile updated successfully");

        return dto;
    }

    @Override
    public UserProfileResponseDto patchProfile(
            UserProfileRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email
                        ));

        if (requestDto.getName() != null
                && !requestDto.getName().isEmpty()) {

            user.setName(requestDto.getName());
        }

        userRepository.save(user);

        System.out.println("[USER PROFILE] Profile patched for: " + email);

        UserProfileResponseDto dto = convertToDto(user);
        dto.setMessage("Profile updated successfully");

        return dto;
    }

    @Override
    public UserProfileResponseDto changePassword(
            ChangePasswordRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email
                        ));

        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        requestDto.getCurrentPassword(),
                        user.getPassword()
                );

        if (!currentPasswordMatches) {
            throw new RuntimeException(
                    "Current password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        requestDto.getNewPassword()
                )
        );

        userRepository.save(user);

        System.out.println("[USER PROFILE] Password changed for: " + email);

        UserProfileResponseDto dto =
                new UserProfileResponseDto();
        dto.setMessage("Password changed successfully");

        return dto;
    }
}