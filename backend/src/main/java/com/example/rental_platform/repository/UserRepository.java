
package com.example.rental_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rental_platform.model.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    // =========================
    // FIND USER BY EMAIL
    // =========================
    Optional<User> findByEmail(String email);

    // =========================
    // CHECK IF EMAIL EXISTS
    // =========================
    boolean existsByEmail(String email);

    // =========================
    // FIND USER BY RESET TOKEN
    // =========================
    Optional<User> findByResetPasswordToken(
            String resetPasswordToken);
}