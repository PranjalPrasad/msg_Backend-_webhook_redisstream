package com.web.webhook.service;

import com.web.webhook.dto.requestDto.SubUserRequestDto;
import com.web.webhook.dto.responseDto.SubUserResponseDto;

import java.util.List;

public interface SubUserService {

    // saare users list karo (admin ke liye)
    List<SubUserResponseDto> getAllUsers();

    // specific user dekho
    SubUserResponseDto getUserById(Long id);

    // naya sub-user create karo
    SubUserResponseDto createSubUser(
            SubUserRequestDto requestDto
    );

    // user update karo (PUT — sab fields)
    SubUserResponseDto updateUser(
            Long id,
            SubUserRequestDto requestDto
    );

    // partial update karo (PATCH — sirf jo field bhejo)
    SubUserResponseDto patchUser(
            Long id,
            SubUserRequestDto requestDto
    );

    // user ka status toggle karo (ACTIVE / INACTIVE)
    SubUserResponseDto toggleUserStatus(
            Long id,
            String status
    );

    // user delete karo
    void deleteUser(Long id);
}