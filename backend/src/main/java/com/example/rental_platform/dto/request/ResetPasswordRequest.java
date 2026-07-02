
package com.example.rental_platform.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    // =========================
    // RESET TOKEN
    // =========================
    private String token;

    // =========================
    // NEW PASSWORD
    // =========================
    private String newPassword;
}