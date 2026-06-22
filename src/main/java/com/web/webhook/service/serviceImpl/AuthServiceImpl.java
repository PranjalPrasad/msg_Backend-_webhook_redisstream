package com.web.webhook.service.serviceImpl;

import com.web.webhook.Enum.Role;
import com.web.webhook.config.JwtUtil;
import com.web.webhook.dto.requestDto.LoginRequestDto;
import com.web.webhook.dto.requestDto.RegisterRequestDto;
import com.web.webhook.dto.responseDto.AuthResponseDto;
import com.web.webhook.entity.User;
import com.web.webhook.repository.UserRepository;
import com.web.webhook.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDto register(
            RegisterRequestDto request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists");
        }

        User user = new User();

        user.setName(
                request.getName());

        user.setEmail(
                request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(
                Role.USER);

        user.setCreatedAt(
                LocalDateTime.now());

        userRepository.save(user);

        AuthResponseDto response =
                new AuthResponseDto();

        response.setMessage(
                "User Registered Successfully");

        return response;
    }

    @Override
    public AuthResponseDto login(
            LoginRequestDto request) {

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid Email"));

        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!matches) {

            throw new RuntimeException(
                    "Invalid Password");
        }

        String token =
                jwtUtil.generateToken(
                        user.getEmail());

        AuthResponseDto response =
                new AuthResponseDto();

        response.setToken(token);

        response.setMessage(
                "Login Successful");

        return response;
    }
}
