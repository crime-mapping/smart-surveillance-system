package com.crimeprevention.smartsurveillancesystem.controllers;

import com.crimeprevention.smartsurveillancesystem.models.User;
import com.crimeprevention.smartsurveillancesystem.services.UserService;
import com.crimeprevention.smartsurveillancesystem.services.TwoFactorService;
import com.crimeprevention.smartsurveillancesystem.types.ERole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import dev.samstevens.totp.code.CodeVerifier;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final TwoFactorService twoFactorService;
    private final BCryptPasswordEncoder encoder;
    private final CodeVerifier codeVerifier;

    public UserController(UserService userService, TwoFactorService twoFactorService, CodeVerifier codeVerifier) {
        this.userService = userService;
        this.twoFactorService = twoFactorService;
        this.codeVerifier = codeVerifier;
        this.encoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            if (!user.getPassword().equals(user.getConfirmPass())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Password Mismatches!"));
            }
            if (userService.emailExists(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Email already Registered!"));
            }

            // Generate 2FA secret for new user
            String secret = twoFactorService.generateTempToken(user);
            user.setTwoFactorSecret(secret);
            user.setRole(ERole.Officer);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setTwoFactorEnabled(true);

            String hashedPassword = encoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            User savedUser = userService.createUser(user);
            if (savedUser != null) {
                return ResponseEntity.ok(new RegistrationResponse(savedUser, secret));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PostMapping("/register/google")
    public ResponseEntity<?> registerByGoogle(@RequestBody User user) {
        try {
            if (userService.emailExists(user.getEmail()) != null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Email already registered!"));
            }
            user.setGoogleAuth(true);
            User savedUser = userService.createUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.emailExists(loginRequest.email());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Credentials!"));
            }

//            if (user.isGoogleAuth()) {
//                return ResponseEntity.badRequest().body(new ErrorResponse("Please use Google login!"));
//            }

            if (!encoder.matches(loginRequest.password(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Credentials!"));
            }

            if (user.isTwoFactorEnabled()) {
                String tempToken = twoFactorService.generateTempToken(user);
                return ResponseEntity.ok(new TwoFactorResponse(tempToken, "2FA Required"));
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> loginByGoogle(@RequestBody User googleUser) {
        try {
            User user = userService.emailExists(googleUser.getEmail());
            if (user == null || !user.isGoogleAuth()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Google account!"));
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PostMapping("/factor")
    public ResponseEntity<?> verifyTwoFactor(@RequestBody TwoFactorRequest request) {
        try {
            // Verify the temporary token
            User user = twoFactorService.validateTempToken(request.tempToken());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid or expired session!"));
            }

            // Verify the 2FA code
            if (!codeVerifier.isValidCode(user.getTwoFactorSecret(), request.code())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid 2FA code!"));
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updateData) {
        try {
            User user = userService.emailExists(updateData.getEmail());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("User not found!"));
            }
            if (updateData.getNames() != null) user.setNames(updateData.getNames());
            if (updateData.getPhone() != null) user.setPhone(updateData.getPhone());

            User updatedUser = userService.updateUser(user.getId(),user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PatchMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            User user = userService.emailExists(request.email());
            if (user == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("User not found!"));
            }

            if (!encoder.matches(request.currentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Current password is incorrect!"));
            }

            if (!codeVerifier.isValidCode(user.getTwoFactorSecret(), request.twoFactorCode())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid 2FA code!"));
            }

            user.setPassword(encoder.encode(request.newPassword()));
            userService.updateUser(user.getId(),user);

            return ResponseEntity.ok(new SuccessResponse("Password updated successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server error!"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Implement any necessary cleanup
        return ResponseEntity.ok(new SuccessResponse("Logged out successfully!"));
    }

    // Request/Response classes
    private record LoginRequest(String email, String password) {}
    private record TwoFactorRequest(String tempToken, String code) {}
    private record ChangePasswordRequest(String email, String currentPassword, String newPassword, String twoFactorCode) {}
    private record TwoFactorResponse(String tempToken, String message) {}
    private record RegistrationResponse(User user, String twoFactorSecret) {}
    private record SuccessResponse(String message) {}
    private record ErrorResponse(String message) {}
}
