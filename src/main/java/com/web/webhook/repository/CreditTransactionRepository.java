package com.web.webhook.repository;

import com.web.webhook.entity.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditTransactionRepository
        extends JpaRepository<CreditTransaction, Long> {

    // credit history — CREDIT type (topup/payments)
    List<CreditTransaction> findByCreatedByAndTypeOrderByCreatedAtDesc(
            String createdBy,
            String type
    );

    // usage history — DEBIT type (message sends)
    List<CreditTransaction> findByCreatedByOrderByCreatedAtDesc(
            String createdBy
    );
}

