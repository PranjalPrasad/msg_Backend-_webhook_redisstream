package com.web.webhook.service;

import com.web.webhook.dto.requestDto.AssignContactRequestDto;
import com.web.webhook.dto.requestDto.GroupRequestDto;
import com.web.webhook.dto.responseDto.ContactResponseDto;
import com.web.webhook.dto.responseDto.GroupResponseDto;

import java.util.List;

public interface GroupService {

    GroupResponseDto createGroup(
            GroupRequestDto requestDto);

    List<GroupResponseDto> getAllGroups();

    GroupResponseDto getGroupById(
            Long id);

    GroupResponseDto updateGroup(
            Long id,
            GroupRequestDto requestDto);

    GroupResponseDto patchGroup(
            Long id,
            GroupRequestDto requestDto);

    void deleteGroup(
            Long id);

    String assignContactToGroup(
            AssignContactRequestDto requestDto);

    List<ContactResponseDto> getGroupContacts(
            Long groupId);
}
