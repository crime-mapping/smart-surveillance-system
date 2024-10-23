package com.crimeprevention.smartsurveillancesystem.services;

import com.crimeprevention.smartsurveillancesystem.models.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.time.Instant;

@Service
public class TwoFactorService {
    private final Map<String, TempToken> tempTokens = new HashMap<>();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static class TempToken {
        User user;
        Instant expiry;

        TempToken(User user) {
            this.user = user;
            this.expiry = Instant.now().plusSeconds(300); // 5 minutes
        }

        boolean isValid() {
            return Instant.now().isBefore(expiry);
        }
    }
    private String generateSecret(int length) {
        StringBuilder secret = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC.length());
            secret.append(ALPHANUMERIC.charAt(index));
        }
        return secret.toString();
    }

    public String generateTempToken(User user) {
        String token = generateSecret(6);
        tempTokens.put(token, new TempToken(user));
        return token;
    }

    public User validateTempToken(String token) {
        TempToken tempToken = tempTokens.get(token);
        if (tempToken != null && tempToken.isValid()) {
            tempTokens.remove(token);
            return tempToken.user;
        }
        return null;
    }
    public void cleanupExpiredTokens() {
        tempTokens.entrySet().removeIf(entry -> !entry.getValue().isValid());
    }
}
