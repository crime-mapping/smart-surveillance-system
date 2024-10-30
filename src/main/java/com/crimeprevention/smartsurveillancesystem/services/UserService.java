package com.crimeprevention.smartsurveillancesystem.services;

import com.crimeprevention.smartsurveillancesystem.models.User;
import com.crimeprevention.smartsurveillancesystem.models.PasswordResetToken;
import com.crimeprevention.smartsurveillancesystem.repositories.PasswordResetTokenRepository;
import com.crimeprevention.smartsurveillancesystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUser(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User emailExists(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User updateUser(long id, User user) {
        User updatedUser = null;
        if (userRepository.existsById(id)) {
            user.setId(id);
             updatedUser = userRepository.save(user);
        }
        return  updatedUser;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getEmail())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String email, String subject,String content) {
           try{
               SimpleMailMessage message = new SimpleMailMessage();
               message.setTo(email);
               message.setSubject(subject);
               message.setText(content);
               mailSender.send(message);
               return true;
           }
           catch (Exception ex){
               ex.printStackTrace();
           }
           return false;
        }

    public boolean sendResetPasswordEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user != null) {
            // Generate token (consider using UUID or JWT)
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setUser(user.get());
            passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

            tokenRepository.save(passwordResetToken);

            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            sendEmail(email,"Password Reset Request","To reset your password, click the link below:\n" + resetLink);
            return true;
        }
        return false;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            PasswordResetToken passwordResetToken = optionalToken.get();
            return LocalDateTime.now().isBefore(passwordResetToken.getExpiryDate());
        }
        return false;
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            PasswordResetToken passwordResetToken = optionalToken.get();
            User user = passwordResetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            tokenRepository.delete(passwordResetToken);
            return true;
        }
        return false;
    }

}