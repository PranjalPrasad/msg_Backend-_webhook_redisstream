package com.web.webhook.controller;

import com.web.webhook.dto.requestDto.AssignContactRequestDto;
import com.web.webhook.dto.requestDto.GroupRequestDto;
import com.web.webhook.dto.responseDto.ContactResponseDto;
import com.web.webhook.dto.responseDto.GroupResponseDto;
import com.web.webhook.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(
            GroupService groupService) {

        this.groupService = groupService;
    }

    @PostMapping("/create-group")
    public ResponseEntity<GroupResponseDto> createGroup(
            @RequestBody GroupRequestDto requestDto){

        return ResponseEntity.ok(
                groupService.createGroup(
                        requestDto));
    }

    @GetMapping("/get-all-groups")
    public ResponseEntity<List<GroupResponseDto>>
    getAllGroups(){

        return ResponseEntity.ok(
                groupService.getAllGroups());
    }

    @GetMapping("/get-group-by-id/{id}")
    public ResponseEntity<GroupResponseDto>
    getGroupById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                groupService.getGroupById(id));
    }

    @PutMapping("/update-group/{id}")
    public ResponseEntity<GroupResponseDto>
    updateGroup(
            @PathVariable Long id,
            @RequestBody GroupRequestDto requestDto){

        return ResponseEntity.ok(
                groupService.updateGroup(
                        id,
                        requestDto));
    }

    @PatchMapping("/patch-group/{id}")
    public ResponseEntity<GroupResponseDto>
    patchGroup(
            @PathVariable Long id,
            @RequestBody GroupRequestDto requestDto){

        return ResponseEntity.ok(
                groupService.patchGroup(
                        id,
                        requestDto));
    }

    @DeleteMapping("/delete-group/{id}")
    public ResponseEntity<String>
    deleteGroup(
            @PathVariable Long id){

        groupService.deleteGroup(id);

        return ResponseEntity.ok(
                "Group Deleted Successfully");
    }

    @PostMapping("/assign-contact")
    public ResponseEntity<String>
    assignContact(
            @RequestBody AssignContactRequestDto requestDto){

        return ResponseEntity.ok(
                groupService.assignContactToGroup(
                        requestDto));
    }

    @GetMapping("/get-group-contacts/{groupId}")
    public ResponseEntity<List<ContactResponseDto>>
    getGroupContacts(
            @PathVariable Long groupId){

        return ResponseEntity.ok(
                groupService.getGroupContacts(
                        groupId));
    }
}