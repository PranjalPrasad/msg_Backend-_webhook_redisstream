
package com.web.webhook.service;

import com.web.webhook.dto.requestDto.AddCreditsRequestDto;
import com.web.webhook.dto.requestDto.UpdatePlanRequestDto;
import com.web.webhook.dto.responseDto.CreditTransactionResponseDto;
import com.web.webhook.dto.responseDto.UserPlanResponseDto;

import java.util.List;

public interface BillingService {

    // current plan + balance info
    UserPlanResponseDto getCurrentPlan();

    // plan create karo (first time setup)
    UserPlanResponseDto createPlan(UpdatePlanRequestDto requestDto);

    // plan upgrade/update karo
    UserPlanResponseDto updatePlan(UpdatePlanRequestDto requestDto);

    // credits add karo (topup)
    UserPlanResponseDto addCredits(AddCreditsRequestDto requestDto);

    // credit history — sirf CREDIT type transactions
    List<CreditTransactionResponseDto> getCreditHistory();

    // usage history — saari transactions (CREDIT + DEBIT)
    List<CreditTransactionResponseDto> getUsageHistory();
}
