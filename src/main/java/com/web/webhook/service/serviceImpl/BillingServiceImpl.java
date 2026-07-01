
package com.web.webhook.service.serviceImpl;

import com.web.webhook.dto.requestDto.AddCreditsRequestDto;
import com.web.webhook.dto.requestDto.UpdatePlanRequestDto;
import com.web.webhook.dto.responseDto.CreditTransactionResponseDto;
import com.web.webhook.dto.responseDto.UserPlanResponseDto;
import com.web.webhook.entity.CreditTransaction;
import com.web.webhook.entity.UserPlan;
import com.web.webhook.repository.CreditTransactionRepository;
import com.web.webhook.repository.UserPlanRepository;
import com.web.webhook.service.BillingService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    private final UserPlanRepository userPlanRepository;
    private final CreditTransactionRepository creditTransactionRepository;

    public BillingServiceImpl(
            UserPlanRepository userPlanRepository,
            CreditTransactionRepository creditTransactionRepository) {

        this.userPlanRepository = userPlanRepository;
        this.creditTransactionRepository = creditTransactionRepository;
    }

    private String getLoggedInUserEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private UserPlanResponseDto convertPlanToDto(UserPlan plan) {

        UserPlanResponseDto dto = new UserPlanResponseDto();
        dto.setId(plan.getId());
        dto.setPlanName(plan.getPlanName());
        dto.setPrice(plan.getPrice());
        dto.setMessageLimit(plan.getMessageLimit());
        dto.setMessagesUsed(plan.getMessagesUsed());
        dto.setCreditBalance(plan.getCreditBalance());
        dto.setValidTill(
                plan.getValidTill() != null
                        ? plan.getValidTill().toString() : null
        );
        dto.setCreatedBy(plan.getCreatedBy());

        return dto;
    }

    private CreditTransactionResponseDto convertTransactionToDto(
            CreditTransaction transaction) {

        CreditTransactionResponseDto dto =
                new CreditTransactionResponseDto();

        dto.setId(transaction.getId());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setBalanceAfter(transaction.getBalanceAfter());
        dto.setDescription(transaction.getDescription());
        dto.setPaymentMethod(transaction.getPaymentMethod());
        dto.setTransactionId(transaction.getTransactionId());
        dto.setCreatedAt(
                transaction.getCreatedAt() != null
                        ? transaction.getCreatedAt().toString() : null
        );

        return dto;
    }

    @Override
    public UserPlanResponseDto getCurrentPlan() {

        String email = getLoggedInUserEmail();

        UserPlan plan = userPlanRepository
                .findByCreatedBy(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No plan found for user: " + email
                                        + ". Please create a plan first."
                        ));

        System.out.println("[BILLING] Plan fetched for: " + email);

        return convertPlanToDto(plan);
    }

    @Override
    public UserPlanResponseDto createPlan(
            UpdatePlanRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        // agar already plan hai toh error do
        if (userPlanRepository.findByCreatedBy(email).isPresent()) {
            throw new RuntimeException(
                    "Plan already exists for user: " + email
                            + ". Use update plan instead."
            );
        }

        UserPlan plan = new UserPlan();
        plan.setCreatedBy(email);
        plan.setPlanName(requestDto.getPlanName());
        plan.setPrice(requestDto.getPrice());
        plan.setMessageLimit(requestDto.getMessageLimit());
        plan.setMessagesUsed(0);
        plan.setCreditBalance(0.0);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());

        if (requestDto.getValidTill() != null) {
            plan.setValidTill(
                    LocalDateTime.parse(requestDto.getValidTill())
            );
        }

        UserPlan saved = userPlanRepository.save(plan);

        System.out.println("[BILLING] Plan created for: " + email
                + " | Plan: " + saved.getPlanName());

        UserPlanResponseDto dto = convertPlanToDto(saved);
        dto.setMessage("Plan created successfully");

        return dto;
    }

    @Override
    public UserPlanResponseDto updatePlan(
            UpdatePlanRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        UserPlan plan = userPlanRepository
                .findByCreatedBy(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No plan found for user: " + email
                        ));

        if (requestDto.getPlanName() != null) {
            plan.setPlanName(requestDto.getPlanName());
        }

        if (requestDto.getPrice() != null) {
            plan.setPrice(requestDto.getPrice());
        }

        if (requestDto.getMessageLimit() != null) {
            plan.setMessageLimit(requestDto.getMessageLimit());
        }

        if (requestDto.getValidTill() != null) {
            plan.setValidTill(
                    LocalDateTime.parse(requestDto.getValidTill())
            );
        }

        plan.setUpdatedAt(LocalDateTime.now());

        UserPlan updated = userPlanRepository.save(plan);

        System.out.println("[BILLING] Plan updated for: " + email
                + " | New plan: " + updated.getPlanName());

        UserPlanResponseDto dto = convertPlanToDto(updated);
        dto.setMessage("Plan updated successfully");

        return dto;
    }

    @Override
    public UserPlanResponseDto addCredits(
            AddCreditsRequestDto requestDto) {

        String email = getLoggedInUserEmail();

        UserPlan plan = userPlanRepository
                .findByCreatedBy(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No plan found for user: " + email
                                        + ". Please create a plan first."
                        ));

        // balance update karo
        Double previousBalance = plan.getCreditBalance();
        Double newBalance = previousBalance + requestDto.getAmount();
        plan.setCreditBalance(newBalance);
        plan.setUpdatedAt(LocalDateTime.now());
        userPlanRepository.save(plan);

        // CREDIT transaction record karo
        CreditTransaction transaction = new CreditTransaction();
        transaction.setCreatedBy(email);
        transaction.setType("CREDIT");
        transaction.setAmount(requestDto.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(
                requestDto.getDescription() != null
                        ? requestDto.getDescription()
                        : "Credits added"
        );
        transaction.setPaymentMethod(requestDto.getPaymentMethod());
        transaction.setTransactionId(requestDto.getTransactionId());
        transaction.setCreatedAt(LocalDateTime.now());
        creditTransactionRepository.save(transaction);

        System.out.println("[BILLING] Credits added for: " + email
                + " | Amount: " + requestDto.getAmount()
                + " | New balance: " + newBalance);

        UserPlanResponseDto dto = convertPlanToDto(plan);
        dto.setMessage("Credits added successfully. New balance: " + newBalance);

        return dto;
    }

    @Override
    public List<CreditTransactionResponseDto> getCreditHistory() {

        String email = getLoggedInUserEmail();

        List<CreditTransaction> transactions =
                creditTransactionRepository
                        .findByCreatedByAndTypeOrderByCreatedAtDesc(
                                email, "CREDIT"
                        );

        List<CreditTransactionResponseDto> result = new ArrayList<>();

        for (CreditTransaction t : transactions) {
            result.add(convertTransactionToDto(t));
        }

        System.out.println("[BILLING] Credit history fetched for: " + email
                + " | Count: " + result.size());

        return result;
    }

    @Override
    public List<CreditTransactionResponseDto> getUsageHistory() {

        String email = getLoggedInUserEmail();

        List<CreditTransaction> transactions =
                creditTransactionRepository
                        .findByCreatedByOrderByCreatedAtDesc(email);

        List<CreditTransactionResponseDto> result = new ArrayList<>();

        for (CreditTransaction t : transactions) {
            result.add(convertTransactionToDto(t));
        }

        System.out.println("[BILLING] Usage history fetched for: " + email
                + " | Count: " + result.size());

        return result;
    }
}
