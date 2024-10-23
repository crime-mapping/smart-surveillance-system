package com.crimeprevention.smartsurveillancesystem.models;

import com.crimeprevention.smartsurveillancesystem.types.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String names;
    private String phone;
    private String email;
    private ERole role;
    private boolean googleAuth;
    private boolean twoFactorEnabled;
    private String twoFactorSecret;
    private String password;
    @Transient
    private String confirmPass;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(long id, String names, String phone, String email, ERole role, boolean googleAuth, boolean twoFactorEnabled, String twoFactorSecret, String password, String confirmPass, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.names = names;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.googleAuth = googleAuth;
        this.twoFactorEnabled = twoFactorEnabled;
        this.twoFactorSecret = twoFactorSecret;
        this.password = password;
        this.confirmPass = confirmPass;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
