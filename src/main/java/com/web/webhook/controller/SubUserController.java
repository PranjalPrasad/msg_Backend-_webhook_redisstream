package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.SubUserRequestDto;
import com.web.webhook.dto.responseDto.SubUserResponseDto;
import com.web.webhook.service.SubUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class SubUserController {

    private final SubUserService subUserService;

    public SubUserController(
            SubUserService subUserService) {

        this.subUserService = subUserService;
    }

    // saare users list karo
    @GetMapping
    public ResponseEntity<List<SubUserResponseDto>>
    getAllUsers() {

        return ResponseEntity.ok(
                subUserService.getAllUsers()
        );
    }

    // specific user dekho
    @GetMapping("/{id}")
    public ResponseEntity<SubUserResponseDto>
    getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                subUserService.getUserById(id)
        );
    }

    // naya sub-user create karo
    @PostMapping
    public ResponseEntity<SubUserResponseDto>
    createSubUser(
            @RequestBody SubUserRequestDto requestDto) {

        return ResponseEntity.ok(
                subUserService.createSubUser(requestDto)
        );
    }

    // user poora update karo (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<SubUserResponseDto>
    updateUser(
            @PathVariable Long id,
            @RequestBody SubUserRequestDto requestDto) {

        return ResponseEntity.ok(
                subUserService.updateUser(id, requestDto)
        );
    }

    // user partial update karo (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<SubUserResponseDto>
    patchUser(
            @PathVariable Long id,
            @RequestBody SubUserRequestDto requestDto) {

        return ResponseEntity.ok(
                subUserService.patchUser(id, requestDto)
        );
    }

    // user status toggle karo — Active/Inactive
    @PatchMapping("/{id}/status")
    public ResponseEntity<SubUserResponseDto>
    toggleUserStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                subUserService.toggleUserStatus(id, status)
        );
    }

    // user delete karo
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteUser(
            @PathVariable Long id) {

        subUserService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}