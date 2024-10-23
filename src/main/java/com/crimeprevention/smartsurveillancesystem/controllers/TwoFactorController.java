package com.crimeprevention.smartsurveillancesystem.controllers;

import com.crimeprevention.smartsurveillancesystem.models.User;
import com.crimeprevention.smartsurveillancesystem.services.TwoFactorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/2fa")
@Slf4j
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    @Autowired
    public TwoFactorController(TwoFactorService twoFactorService) {
        this.twoFactorService = twoFactorService;
    }

    // DTO for token request response
    private static class TokenResponse {
        private final String token;
        private final String message;

        public TokenResponse(String token, String message) {
            this.token = token;
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public String getMessage() {
            return message;
        }
    }

    // DTO for token validation request
    private static class TokenValidationRequest {
//        @NotBlank(message = "Token cannot be empty")
//        @Pattern(regexp = "^[A-Z0-9]{6}$", message = "Invalid token format")
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<TokenResponse> generateToken(@AuthenticationPrincipal User user) {
        try {
            log.info("Generating 2FA token for user with email: {}", user.getEmail());
            String token = twoFactorService.generateTempToken(user);

            // In a real application, you would typically send this token via SMS/email
            // For demonstration, we're returning it in the response
            return ResponseEntity.ok(new TokenResponse(token,
                    "Token has been generated and sent to your registered device"));
        } catch (Exception e) {
            log.error("Error generating 2FA token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TokenResponse(null, "Error generating token"));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(
             @RequestBody TokenValidationRequest request) {
        try {
            log.info("Validating 2FA token");
            User user = twoFactorService.validateTempToken(request.getToken());

            if (user != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Token validated successfully");
                response.put("email", user.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid or expired token"));
            }
        } catch (Exception e) {
            log.error("Error validating 2FA token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error validating token"));
        }
    }

    @PostMapping("/cleanup")
    public ResponseEntity<?> cleanupExpiredTokens() {
        try {
            log.info("Cleaning up expired 2FA tokens");
            twoFactorService.cleanupExpiredTokens();
            return ResponseEntity.ok(Map.of("message", "Expired tokens cleaned up successfully"));
        } catch (Exception e) {
            log.error("Error cleaning up expired tokens", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error cleaning up expired tokens"));
        }
    }

}