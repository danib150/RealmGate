package com.realmgate.security;

import com.realmgate.server.BackendServer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

public final class PayloadSigner {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SEPARATOR = "|";

    private final SecretKeySpec key;

    public PayloadSigner(String secretKey) {
        this.key = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                HMAC_ALGO
        );
    }

    /**
     * Payload format:
     * uuid|server|timestamp|signature(base64)
     */
    public byte[] sign(UUID playerId, BackendServer server) {
        long timestamp = System.currentTimeMillis();

        String data = playerId + SEPARATOR + server.getName() + SEPARATOR + timestamp;

        byte[] signature = hmac(data);

        String payload = data + SEPARATOR + Base64.getEncoder().encodeToString(signature);

        return payload.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Verifies payload integrity and signature.
     */
    public boolean verify(byte[] payloadBytes) {
        try {
            String payload = new String(payloadBytes, StandardCharsets.UTF_8);
            String[] parts = payload.split("\\|");

            if (parts.length != 4) {
                return false;
            }

            String data = parts[0] + SEPARATOR + parts[1] + SEPARATOR + parts[2];

            byte[] receivedSig = Base64.getDecoder().decode(parts[3]);
            byte[] expectedSig = hmac(data);

            return MessageDigest.isEqual(receivedSig, expectedSig);
        } catch (Exception e) {
            return false;
        }
    }

    private byte[] hmac(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(key);
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate HMAC", e);
        }
    }

    public boolean isFresh(byte[] payloadBytes, long maxAgeMs) {
        try {
            String payload = new String(payloadBytes, StandardCharsets.UTF_8);
            String[] parts = payload.split("\\|");

            long timestamp = Long.parseLong(parts[2]);
            return System.currentTimeMillis() - timestamp <= maxAgeMs;
        } catch (Exception e) {
            return false;
        }
    }

}
