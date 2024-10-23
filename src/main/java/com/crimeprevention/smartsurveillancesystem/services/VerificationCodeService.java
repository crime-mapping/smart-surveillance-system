package com.crimeprevention.smartsurveillancesystem.services;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VerificationCodeService {
    private final Logger logger = LoggerFactory.getLogger(VerificationCodeService.class);
    private final SecureRandom secureRandom = new SecureRandom();
    private final ConcurrentHashMap<String, CodeDetails> activeCodes = new ConcurrentHashMap<>();
    private final EmailService emailService;

    // Constants
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;

    public VerificationCodeService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void generateAndSendCode(String email, String purpose) {
        String code = generateCode();
        String key = generateKey(email, purpose);

        // Store the code with details
        activeCodes.put(key, new CodeDetails(code));

        // Send the code via email
        emailService.sendVerificationCode(email, code, purpose);

        logger.info("Verification code generated and sent for: {}", email);
    }

    public boolean verifyCode(String email, String code, String purpose) {
        String key = generateKey(email, purpose);
        CodeDetails details = activeCodes.get(key);

        if (details == null) {
            logger.warn("No active code found for: {}", email);
            return false;
        }

        // Check if code is expired
        if (details.isExpired()) {
            activeCodes.remove(key);
            logger.warn("Code expired for: {}", email);
            return false;
        }

        // Increment attempt counter
        details.incrementAttempts();

        // Check if max attempts exceeded
        if (details.isMaxAttemptsExceeded()) {
            activeCodes.remove(key);
            logger.warn("Max attempts exceeded for: {}", email);
            return false;
        }

        // Verify the code
        boolean isValid = details.code.equals(code);

        if (isValid) {
            activeCodes.remove(key);
            logger.info("Code verified successfully for: {}", email);
        } else {
            logger.warn("Invalid code attempt for: {}", email);
        }

        return isValid;
    }

    private String generateCode() {
        // Generate a random 6-digit code
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }

    private String generateKey(String email, String purpose) {
        return email + ":" + purpose;
    }

    // Cleanup method to remove expired codes (can be scheduled)
    public void cleanupExpiredCodes() {
        activeCodes.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static class CodeDetails {
        private final String code;
        private final Instant expiryTime;
        private int attempts;

        CodeDetails(String code) {
            this.code = code;
            this.expiryTime = Instant.now().plusSeconds(CODE_EXPIRY_MINUTES * 60);
            this.attempts = 0;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiryTime);
        }

        void incrementAttempts() {
            this.attempts++;
        }

        boolean isMaxAttemptsExceeded() {
            return attempts >= MAX_ATTEMPTS;
        }
    }
}