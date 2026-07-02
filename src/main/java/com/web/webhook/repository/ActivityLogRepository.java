package com.web.webhook.repository;

import com.web.webhook.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository
        extends JpaRepository<ActivityLog, Long> {

    // saare logs latest first
    List<ActivityLog> findAllByOrderByCreatedAtDesc();

    // specific user ke logs
    List<ActivityLog> findByUserEmailOrderByCreatedAtDesc(
            String userEmail
    );

    // specific module ke logs
    List<ActivityLog> findByModuleOrderByCreatedAtDesc(
            String module
    );

    // specific action ke logs
    List<ActivityLog> findByActionOrderByCreatedAtDesc(
            String action
    );

    // user + module filter
    List<ActivityLog> findByUserEmailAndModuleOrderByCreatedAtDesc(
            String userEmail,
            String module
    );
}