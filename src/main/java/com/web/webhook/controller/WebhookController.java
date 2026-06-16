package com.web.webhook.controller;
import com.web.webhook.service.RedisProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.webhook.config.AppConfig;
import com.web.webhook.dto.requestDto.WebhookRequestDto;
import com.web.webhook.dto.responseDto.WebhookResponseDto;
import com.web.webhook.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;
    private final AppConfig appConfig;
    private final ObjectMapper objectMapper;
    private final RedisProducerService redisProducerService;

    public WebhookController(
            WebhookService webhookService,
            AppConfig appConfig,
            ObjectMapper objectMapper,
            RedisProducerService redisProducerService) {

        this.webhookService = webhookService;
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
        this.redisProducerService = redisProducerService;
    }

    // Meta calls this GET to verify your webhook URL
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && appConfig.getVerifyToken().equals(token)) {
            System.out.println("Webhook verified successfully!");
            return ResponseEntity.ok(challenge); // Must return challenge as plain string
        }

        return ResponseEntity.status(403).body("Verification failed");
    }

    // Meta sends delivery/read/incoming events here
    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody String rawPayload) {
        try {
            WebhookRequestDto dto = objectMapper.readValue(rawPayload, WebhookRequestDto.class);
            redisProducerService.publishWebhook(
                    rawPayload
            );
            return ResponseEntity.ok("EVENT_RECEIVED"); // Meta expects exactly this
        } catch (Exception e) {
            System.err.println("Webhook parse error: " + e.getMessage());
            return ResponseEntity.ok("EVENT_RECEIVED"); // Always return 200 to Meta
        }
    }

    // Your own API to see what was received
    @GetMapping("/get-logs/logs")
    public ResponseEntity<List<WebhookResponseDto>> getLogs() {
        return ResponseEntity.ok(webhookService.getAllLogs());
    }

    @PostMapping("/test")
    public ResponseEntity<String> testWebhook(
            @RequestBody String payload) {

        try {

            WebhookRequestDto dto =
                    objectMapper.readValue(
                            payload,
                            WebhookRequestDto.class);

            webhookService.processWebhook(dto,payload);

            return ResponseEntity.ok(
                    "Webhook Processed Successfully");

        } catch (Exception e){

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
