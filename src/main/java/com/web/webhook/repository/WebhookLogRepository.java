package com.web.webhook.repository;
import java.util.Optional;
import com.web.webhook.entity.WebhookLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WebhookLogRepository
        extends JpaRepository<WebhookLog, Long> {

    Optional<WebhookLog> findByMessageIdAndStatus(
            String messageId,
            String status);
}
