package com.web.webhook.repository;

import com.web.webhook.entity.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPlanRepository
        extends JpaRepository<UserPlan, Long> {

    Optional<UserPlan> findByCreatedBy(String createdBy);
}

