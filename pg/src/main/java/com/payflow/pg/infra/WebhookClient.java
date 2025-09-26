package com.payflow.pg.infra;

import com.payflow.pg.config.AppProperties;
import com.payflow.pg.dto.WebhookPayload;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class WebhookClient {
    private final RestClient rest;
    private final String secret;

    public WebhookClient(AppProperties props) {
        this.rest = RestClient.builder().baseUrl(props.getMerchantWebhookUrl()).build();
        this.secret = props.getWebhookSecret();
    }

    public void send(WebhookPayload payload) {
        String signature = sign(payload.getTid() + "|" + payload.getOrderNo() + "|" + payload.getAmount() + "|" + payload.getState());
        try {
            rest.post().uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Payflow-Signature", signature)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ignored) {
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
