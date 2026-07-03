package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.AutoReplyRequestDto;
import com.web.webhook.dto.responseDto.AutoReplyResponseDto;
import com.web.webhook.entity.AutoReplyRule;
import com.web.webhook.repository.AutoReplyRuleRepository;
import com.web.webhook.service.AutoReplyService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AutoReplyServiceImpl
        implements AutoReplyService {

    private final AutoReplyRuleRepository autoReplyRuleRepository;

    public AutoReplyServiceImpl(
            AutoReplyRuleRepository autoReplyRuleRepository) {

        this.autoReplyRuleRepository =
                autoReplyRuleRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private AutoReplyResponseDto convertToDto(
            AutoReplyRule rule) {

        AutoReplyResponseDto dto =
                new AutoReplyResponseDto();

        dto.setId(rule.getId());
        dto.setRuleName(rule.getRuleName());
        dto.setKeyword(rule.getKeyword());
        dto.setReplyText(rule.getReplyText());
        dto.setStatus(rule.getStatus());
        dto.setCreatedBy(rule.getCreatedBy());
        dto.setCreatedAt(
                rule.getCreatedAt() != null
                        ? rule.getCreatedAt().toString()
                        : null
        );
        dto.setUpdatedAt(
                rule.getUpdatedAt() != null
                        ? rule.getUpdatedAt().toString()
                        : null
        );

        return dto;
    }

    @Override
    public AutoReplyResponseDto createRule(
            AutoReplyRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        AutoReplyRule rule = new AutoReplyRule();
        rule.setRuleName(requestDto.getRuleName());
        rule.setKeyword(requestDto.getKeyword());
        rule.setReplyText(requestDto.getReplyText());
        rule.setStatus(
                requestDto.getStatus() != null
                        ? requestDto.getStatus()
                        : "ACTIVE"
        );
        rule.setCreatedBy(email);
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());

        AutoReplyRule saved =
                autoReplyRuleRepository.save(rule);

        System.out.println("[AUTO REPLY] Rule created: "
                + saved.getRuleName()
                + " | Keyword: " + saved.getKeyword()
                + " | By: " + email);

        return convertToDto(saved);
    }

    @Override
    public List<AutoReplyResponseDto> getAllRules() {

        String email = getLoggedInUserEmail();

        System.out.println("[AUTO REPLY] Fetching rules for: " + email);

        List<AutoReplyRule> rules =
                autoReplyRuleRepository.findByCreatedBy(email);

        System.out.println("[AUTO REPLY] Rules found: " + rules.size());

        List<AutoReplyResponseDto> result =
                new ArrayList<>();

        for (AutoReplyRule rule : rules) {
            result.add(convertToDto(rule));
        }

        return result;
    }

    @Override
    public AutoReplyResponseDto getRuleById(Long id) {

        String email = getLoggedInUserEmail();

        AutoReplyRule rule = autoReplyRuleRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Auto reply rule not found with id: " + id
                        ));

        return convertToDto(rule);
    }

    @Override
    public AutoReplyResponseDto updateRule(
            Long id,
            AutoReplyRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        AutoReplyRule rule = autoReplyRuleRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Auto reply rule not found with id: " + id
                        ));

        rule.setRuleName(requestDto.getRuleName());
        rule.setKeyword(requestDto.getKeyword());
        rule.setReplyText(requestDto.getReplyText());
        rule.setStatus(requestDto.getStatus());
        rule.setUpdatedAt(LocalDateTime.now());

        AutoReplyRule updated =
                autoReplyRuleRepository.save(rule);

        System.out.println("[AUTO REPLY] Rule updated: "
                + updated.getId());

        return convertToDto(updated);
    }

    @Override
    public AutoReplyResponseDto patchRule(
            Long id,
            AutoReplyRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        AutoReplyRule rule = autoReplyRuleRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Auto reply rule not found with id: " + id
                        ));

        if (requestDto.getRuleName() != null) {
            rule.setRuleName(requestDto.getRuleName());
        }

        if (requestDto.getKeyword() != null) {
            rule.setKeyword(requestDto.getKeyword());
        }

        if (requestDto.getReplyText() != null) {
            rule.setReplyText(requestDto.getReplyText());
        }

        if (requestDto.getStatus() != null) {
            rule.setStatus(requestDto.getStatus());
        }

        rule.setUpdatedAt(LocalDateTime.now());

        AutoReplyRule patched =
                autoReplyRuleRepository.save(rule);

        System.out.println("[AUTO REPLY] Rule patched: "
                + patched.getId());

        return convertToDto(patched);
    }

    @Override
    public void deleteRule(Long id) {

        String email = getLoggedInUserEmail();

        AutoReplyRule rule = autoReplyRuleRepository
                .findByIdAndCreatedBy(id, email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Auto reply rule not found with id: " + id
                        ));

        autoReplyRuleRepository.delete(rule);

        System.out.println("[AUTO REPLY] Rule deleted: " + id);
    }

    @Override
    public String findMatchingReply(
            String incomingMessageText) {

        if (incomingMessageText == null
                || incomingMessageText.isEmpty()) {
            return null;
        }

        List<AutoReplyRule> activeRules =
                autoReplyRuleRepository.findByStatus("ACTIVE");

        String lowerCaseMessage =
                incomingMessageText.toLowerCase();

        for (AutoReplyRule rule : activeRules) {

            if (rule.getKeyword() != null
                    && lowerCaseMessage.contains(
                    rule.getKeyword().toLowerCase())) {

                System.out.println(
                        "[AUTO REPLY] Keyword matched: "
                                + rule.getKeyword()
                                + " | Rule: " + rule.getRuleName()
                );

                return rule.getReplyText();
            }
        }

        System.out.println(
                "[AUTO REPLY] No matching rule found for: "
                        + incomingMessageText
        );

        return null;
    }
}