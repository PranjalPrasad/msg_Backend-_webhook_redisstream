package com.web.webhook.service.serviceImpl;
import com.web.webhook.dto.requestDto.AssignContactRequestDto;
import com.web.webhook.dto.requestDto.GroupRequestDto;
import com.web.webhook.dto.responseDto.ContactResponseDto;
import com.web.webhook.dto.responseDto.GroupResponseDto;

import com.web.webhook.entity.Contact;
import com.web.webhook.entity.ContactGroup;

import com.web.webhook.entity.ContactGroupMapping;
import com.web.webhook.repository.ContactGroupMappingRepository;
import com.web.webhook.repository.ContactGroupRepository;
import com.web.webhook.repository.ContactRepository;
import com.web.webhook.service.GroupService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl
        implements GroupService {

    private final ContactGroupRepository groupRepository;
    private final ContactRepository contactRepository;
    private final ContactGroupMappingRepository mappingRepository;

    public GroupServiceImpl(
            ContactGroupRepository groupRepository,
            ContactRepository contactRepository,
            ContactGroupMappingRepository mappingRepository) {

        this.groupRepository = groupRepository;
        this.contactRepository = contactRepository;
        this.mappingRepository = mappingRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private GroupResponseDto convertToDto(
            ContactGroup group) {

        GroupResponseDto dto =
                new GroupResponseDto();

        dto.setId(group.getId());
        dto.setGroupName(group.getGroupName());
        dto.setDescription(group.getDescription());

        return dto;
    }

    @Override
    public GroupResponseDto createGroup(
            GroupRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        if(groupRepository
                .existsByGroupNameAndCreatedBy(
                        requestDto.getGroupName(),
                        email
                )) {

            throw new RuntimeException(
                    "Group Already Exists"
            );
        }

        ContactGroup group =
                new ContactGroup();

        group.setGroupName(
                requestDto.getGroupName());

        group.setDescription(
                requestDto.getDescription());

        group.setCreatedBy(email);

        group.setCreatedAt(
                LocalDateTime.now());

        group.setUpdatedAt(
                LocalDateTime.now());

        ContactGroup saved =
                groupRepository.save(group);

        return convertToDto(saved);
    }

    @Override
    public List<GroupResponseDto> getAllGroups() {

        String email =
                getLoggedInUserEmail();

        List<ContactGroup> groups =
                groupRepository.findByCreatedBy(
                        email
                );

        List<GroupResponseDto> result =
                new ArrayList<>();

        for(ContactGroup group : groups){

            result.add(
                    convertToDto(group)
            );
        }

        return result;
    }

    @Override
    public GroupResponseDto getGroupById(
            Long id) {

        String email =
                getLoggedInUserEmail();

        ContactGroup group =
                groupRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Group Not Found"
                                )
                        );

        return convertToDto(group);
    }

    @Override
    public GroupResponseDto updateGroup(
            Long id,
            GroupRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        ContactGroup group =
                groupRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Group Not Found"
                                )
                        );

        group.setGroupName(
                requestDto.getGroupName()
        );

        group.setDescription(
                requestDto.getDescription()
        );

        group.setUpdatedAt(
                LocalDateTime.now()
        );

        ContactGroup updated =
                groupRepository.save(group);

        return convertToDto(updated);
    }

    @Override
    public GroupResponseDto patchGroup(
            Long id,
            GroupRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        ContactGroup group =
                groupRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Group Not Found"
                                )
                        );

        if(requestDto.getGroupName() != null){

            group.setGroupName(
                    requestDto.getGroupName()
            );
        }

        if(requestDto.getDescription() != null){

            group.setDescription(
                    requestDto.getDescription()
            );
        }

        group.setUpdatedAt(
                LocalDateTime.now()
        );

        ContactGroup updated =
                groupRepository.save(group);

        return convertToDto(updated);
    }

    @Override
    public void deleteGroup(
            Long id) {

        String email =
                getLoggedInUserEmail();

        ContactGroup group =
                groupRepository
                        .findByIdAndCreatedBy(
                                id,
                                email
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Group Not Found"
                                )
                        );

        groupRepository.delete(group);
    }

    @Override
    public String assignContactToGroup(
            AssignContactRequestDto requestDto) {

        String email =
                getLoggedInUserEmail();

        System.out.println("===== DEBUG =====");
        System.out.println("Logged In User = " + email);
        System.out.println("Contact Id = " + requestDto.getContactId());
        System.out.println("Group Id = " + requestDto.getGroupId());

        System.out.println(
                contactRepository.findByIdAndCreatedBy(
                        requestDto.getContactId(),
                        email
                )
        );

        contactRepository
                .findByIdAndCreatedBy(
                        requestDto.getContactId(),
                        email
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Contact Not Found"
                        )
                );

        groupRepository
                .findByIdAndCreatedBy(
                        requestDto.getGroupId(),
                        email
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Group Not Found"
                        )
                );

        if(mappingRepository
                .existsByContactIdAndGroupId(
                        requestDto.getContactId(),
                        requestDto.getGroupId()
                )) {

            throw new RuntimeException(
                    "Contact Already Assigned"
            );
        }

        ContactGroupMapping mapping =
                new ContactGroupMapping();

        mapping.setContactId(
                requestDto.getContactId()
        );

        mapping.setGroupId(
                requestDto.getGroupId()
        );

        mappingRepository.save(mapping);

        return "Contact Assigned Successfully";
    }

    @Override
    public List<ContactResponseDto> getGroupContacts(
            Long groupId) {

        String email =
                getLoggedInUserEmail();

        groupRepository
                .findByIdAndCreatedBy(
                        groupId,
                        email
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Group Not Found"
                        )
                );

        List<ContactGroupMapping> mappings =
                mappingRepository.findByGroupId(
                        groupId
                );

        List<Long> contactIds =
                new ArrayList<>();

        for(ContactGroupMapping mapping : mappings){

            contactIds.add(
                    mapping.getContactId()
            );
        }

        List<Contact> contacts =
                contactRepository.findByIdIn(
                        contactIds
                );

        List<ContactResponseDto> result =
                new ArrayList<>();

        for(Contact contact : contacts){

            ContactResponseDto dto =
                    new ContactResponseDto();

            dto.setId(contact.getId());
            dto.setName(contact.getName());
            dto.setPhoneNumber(contact.getPhoneNumber());
            dto.setEmail(contact.getEmail());
            dto.setCity(contact.getCity());
            dto.setStatus(contact.getStatus());

            result.add(dto);
        }

        return result;
    }
}
