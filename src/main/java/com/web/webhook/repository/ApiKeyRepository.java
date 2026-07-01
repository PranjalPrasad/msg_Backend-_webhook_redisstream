package com.web.webhook.repository;

import com.web.webhook.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository
        extends JpaRepository<ApiKey, Long> {

    List<ApiKey> findByCreatedBy(
            String createdBy
    );

    Optional<ApiKey> findByIdAndCreatedBy(
            Long id,
            String createdBy
    );

    Optional<ApiKey> findByKeyValue(
            String keyValue
    );

    boolean existsByKeyValue(
            String keyValue
    );
}