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

        this.whatsappAccountRepository =
                whatsappAccountRepository;
    }

    @Override
    public String sendMessage(WhatsappMessage message) {

        try {

            // step 1: user ka whatsapp account fetch karo
            // accessToken aur phoneNumberId yahan se aayega
            List<WhatsappAccount> accounts =
                    whatsappAccountRepository
                            .findByCreatedBy(message.getCreatedBy());

            if (accounts == null || accounts.isEmpty()) {
                System.out.println(
                        "WhatsApp account nahi mila user ke liye: "
                                + message.getCreatedBy()
                );
                return null;
            }

            // pehla active account use karo
            WhatsappAccount account = accounts.get(0);

            String phoneNumberId = account.getPhoneNumberId();
            String accessToken = account.getAccessToken();

            // ================================================================
            // META API CALL — END MEIN CONFIGURE HOGI
            // ================================================================
            // Jab Meta ka access token aur phoneNumberId milega
            // tab neeche ka code uncomment karna:
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
            //         + "\"text\": { \"preview_url\": false, "
            //         + "\"body\": \"" + message.getMessageBody() + "\" }"
            //         + "}";
            //
            // HttpEntity<String> entity =
            //         new HttpEntity<>(requestBody, headers);
            //
            // RestTemplate restTemplate = new RestTemplate();
            // ResponseEntity<String> response =
            //         restTemplate.postForEntity(url, entity, String.class);
            //
            // ObjectMapper mapper = new ObjectMapper();
            // JsonNode root = mapper.readTree(response.getBody());
            // String wamid = root.path("messages")
            //         .get(0).path("id").asText();
            // return wamid;
            // ================================================================

            // ABHI KE LIYE PLACEHOLDER:
            // Meta configure nahi hai toh dummy ID return karte hain
            // isse poora flow test ho sakta hai — Redis, DB update, campaign counter
            // Meta configure hone ke baad yeh line hatana aur upar wala uncomment karna
            System.out.println(
                    "META PLACEHOLDER: To="
                            + message.getPhoneNumber()
                            + " | Body=" + message.getMessageBody()
            );

            return "DUMMY_WAMID_" + message.getId();

        } catch (Exception e) {
            System.err.println(
                    "MetaApiService error: " + e.getMessage()
            );
            e.printStackTrace();
            return null;
        }
    }
}
