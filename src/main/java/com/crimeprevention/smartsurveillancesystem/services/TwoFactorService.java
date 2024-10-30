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
    private static final SecureRandom RANDOM = new SecureRandom();

    // Inner class for TempToken
    private static class TempToken {
        private final User user;
        private final Instant expiry;

        TempToken(User user) {
            this.user = user;
            this.expiry = Instant.now().plusSeconds(300);
        }

        boolean isValid() {
            return Instant.now().isBefore(expiry);
        }
    }

    private String generateCode() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    public String generateTempToken(User user) {
        String code = generateCode();
        tempTokens.put(code, new TempToken(user));
        return code;
    }

    public User validateTempToken(String code) {
        TempToken tempToken = tempTokens.get(code);
        if (tempToken != null && tempToken.isValid()) {
            tempTokens.remove(code); // Remove once validated
            return tempToken.user;
        }
        return null;
    }

    // Clean up expired tokens
    public void cleanupExpiredTokens() {
        tempTokens.entrySet().removeIf(entry -> !entry.getValue().isValid());
    }
}
