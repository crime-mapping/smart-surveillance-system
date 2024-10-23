package com.crimeprevention.smartsurveillancesystem.services;

import com.crimeprevention.smartsurveillancesystem.models.User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

@Service
public class TwoFactorService {
    private final Map<String, TempToken> tempTokens = new HashMap<>();

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

    public String generateTempToken(User user) {
        String token = UUID.randomUUID().toString();
        tempTokens.put(token, new TempToken(user));
        return token;
    }

    public User validateTempToken(String token) {
        TempToken tempToken = tempTokens.get(token);
        if (tempToken != null && tempToken.isValid()) {
            tempTokens.remove(token); // One-time use
            return tempToken.user;
        }
        return null;
    }

    // Cleanup method to be called periodically
    public void cleanupExpiredTokens() {
        tempTokens.entrySet().removeIf(entry -> !entry.getValue().isValid());
    }
}
