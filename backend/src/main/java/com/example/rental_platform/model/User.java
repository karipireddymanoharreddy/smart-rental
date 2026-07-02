
package com.example.rental_platform.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // =========================
    // PRIMARY KEY
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // USER DETAILS
    // =========================
    private String name;

    private String email;

    private String password;

    private String phone;

    // =========================
    // PASSWORD RESET TOKEN
    // =========================
    private String resetPasswordToken;

    // =========================
    // RESET TOKEN EXPIRY
    // =========================
    private LocalDateTime resetPasswordTokenExpiry;
}