package com.web.webhook.service.serviceImpl;

import com.web.webhook.entity.WhatsappAccount;
import com.web.webhook.entity.WhatsappMessage;
import com.web.webhook.repository.WhatsappAccountRepository;
import com.web.webhook.service.MetaApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaApiServiceImpl implements MetaApiService {

    private final WhatsappAccountRepository whatsappAccountRepository;

    public MetaApiServiceImpl(
            WhatsappAccountRepository whatsappAccountRepository) {
        this.whatsappAccountRepository = whatsappAccountRepository;
    }

    @Override
    public String sendMessage(WhatsappMessage message) {

        try {

            List<WhatsappAccount> accounts =
                    whatsappAccountRepository.findByCreatedBy(message.getCreatedBy());

            if (accounts == null || accounts.isEmpty()) {
                System.err.println("No WhatsApp account found for user: " + message.getCreatedBy());
                return null;
            }

            WhatsappAccount account = accounts.get(0);

            String phoneNumberId = account.getPhoneNumberId();
            String accessToken = account.getAccessToken();

            // ================================================================
            // META API CALL — TO BE CONFIGURED AT THE END OF THE PROJECT
            // ================================================================
            // When Meta access token and phoneNumberId are available,
            // uncomment the below code and remove the placeholder return:
            //
            // String url = "https://graph.facebook.com/v18.0/"
            //         + phoneNumberId + "/messages";
            //
            // HttpHeaders headers = new HttpHeaders();
            // headers.set("Authorization", "Bearer " + accessToken);
            // headers.setContentType(MediaType.APPLICATION_JSON);
            //
            // String requestBody = "{"
            //         + "\"messaging_product\": \"whatsapp\","
            //         + "\"recipient_type\": \"individual\","
            //         + "\"to\": \"" + message.getPhoneNumber() + "\","
            //         + "\"type\": \"text\","
            //         + "\"text\": { \"preview_url\": false,"
            //         + " \"body\": \"" + message.getMessageBody() + "\" }"
            //         + "}";
            //
            // HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            // RestTemplate restTemplate = new RestTemplate();
            // ResponseEntity<String> response =
            //         restTemplate.postForEntity(url, entity, String.class);
            //
            // ObjectMapper mapper = new ObjectMapper();
            // JsonNode root = mapper.readTree(response.getBody());
            // String wamid = root.path("messages").get(0).path("id").asText();
            // System.out.println("Message sent successfully. WAMID: " + wamid);
            // return wamid;
            // ================================================================

            // PLACEHOLDER — Remove this block once Meta is configured
            System.out.println("[META PLACEHOLDER] Message queued for sending."
                    + " To: " + message.getPhoneNumber()
                    + " | Body: " + message.getMessageBody()
                    + " | Account: " + phoneNumberId);

            return "DUMMY_WAMID_" + message.getId();

        } catch (Exception e) {
            System.err.println("MetaApiService error for message ID "
                    + message.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}