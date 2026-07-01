package com.web.webhook.service.serviceImpl;

import com.web.webhook.Enum.Role;
import com.web.webhook.dto.requestDto.SubUserRequestDto;
import com.web.webhook.dto.responseDto.SubUserResponseDto;
import com.web.webhook.entity.User;
import com.web.webhook.repository.UserRepository;
import com.web.webhook.service.SubUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubUserServiceImpl
        implements SubUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SubUserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private SubUserResponseDto convertToDto(User user) {

        SubUserResponseDto dto = new SubUserResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(
                user.getRole() != null
                        ? user.getRole().name() : null
        );
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(
                user.getCreatedAt() != null
                        ? user.getCreatedAt().toString() : null
        );

        return dto;
    }

    @Override
    public List<SubUserResponseDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<SubUserResponseDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(convertToDto(user));
        }

        System.out.println("[SUB USERS] Fetched all users. Count: "
                + result.size());

        return result;
    }

    @Override
    public SubUserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        return convertToDto(user);
    }

    @Override
    public SubUserResponseDto createSubUser(
            SubUserRequestDto requestDto) {

        if (userRepository.existsByEmail(
                requestDto.getEmail())) {

            throw new RuntimeException(
                    "Email already exists: " + requestDto.getEmail()
            );
        }

        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        requestDto.getPassword()
                )
        );

        // role set karo — default USER rahega agar kuch specify nahi kiya
        if (requestDto.getRole() != null
                && requestDto.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        // default status ACTIVE
        user.setStatus(
                requestDto.getStatus() != null
                        ? requestDto.getStatus() : "ACTIVE"
        );

        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        System.out.println("[SUB USERS] New sub-user created: "
                + saved.getEmail() + " | Role: " + saved.getRole());

        SubUserResponseDto dto = convertToDto(saved);
        dto.setMessage("Sub-user created successfully");

        return dto;
    }

    @Override
    public SubUserResponseDto updateUser(
            Long id,
            SubUserRequestDto requestDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        user.setName(requestDto.getName());

        if (requestDto.getPassword() != null
                && !requestDto.getPassword().isEmpty()) {

            user.setPassword(
                    passwordEncoder.encode(
                            requestDto.getPassword()
                    )
            );
        }

        if (requestDto.getRole() != null) {
            if (requestDto.getRole().equalsIgnoreCase("ADMIN")) {
                user.setRole(Role.ADMIN);
            } else {
                user.setRole(Role.USER);
            }
        }

        if (requestDto.getStatus() != null) {
            user.setStatus(requestDto.getStatus());
        }

        User updated = userRepository.save(user);

        System.out.println("[SUB USERS] User updated: " + updated.getId());

        SubUserResponseDto dto = convertToDto(updated);
        dto.setMessage("User updated successfully");

        return dto;
    }

    @Override
    public SubUserResponseDto patchUser(
            Long id,
            SubUserRequestDto requestDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        // sirf jo field bheja ho wahi update karo
        if (requestDto.getName() != null) {
            user.setName(requestDto.getName());
        }

        if (requestDto.getPassword() != null
                && !requestDto.getPassword().isEmpty()) {

            user.setPassword(
                    passwordEncoder.encode(
                            requestDto.getPassword()
                    )
            );
        }

        if (requestDto.getRole() != null) {
            if (requestDto.getRole().equalsIgnoreCase("ADMIN")) {
                user.setRole(Role.ADMIN);
            } else {
                user.setRole(Role.USER);
            }
        }

        if (requestDto.getStatus() != null) {
            user.setStatus(requestDto.getStatus());
        }

        User patched = userRepository.save(user);

        System.out.println("[SUB USERS] User patched: " + patched.getId());

        SubUserResponseDto dto = convertToDto(patched);
        dto.setMessage("User updated successfully");

        return dto;
    }

    @Override
    public SubUserResponseDto toggleUserStatus(
            Long id,
            String status) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        user.setStatus(status);

        User updated = userRepository.save(user);

        System.out.println("[SUB USERS] User status toggled: "
                + updated.getId() + " | New status: " + status);

        SubUserResponseDto dto = convertToDto(updated);
        dto.setMessage("User status updated to " + status);

        return dto;
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));

        userRepository.delete(user);

        System.out.println("[SUB USERS] User deleted: " + id);
    }
}