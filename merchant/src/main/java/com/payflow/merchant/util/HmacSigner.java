package com.payflow.merchant.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacSigner {
    public static String sign(String secret, String body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(mac.doFinal(body.getBytes()));
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
}
