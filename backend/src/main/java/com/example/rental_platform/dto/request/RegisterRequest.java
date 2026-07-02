
package com.example.rental_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    // =========================
    // NAME
    // =========================
    @Schema(
            description = "Full name of the user",
            example = "manohar reddy")
    @NotBlank(message = "Name is required")
    private String name;

    // =========================
    // EMAIL
    // =========================
    @Schema(
            description = "Email address",
            example = "karipiredddymanoharreddy@gmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // =========================
    // PASSWORD
    // =========================
    @Schema(
            description = "User password",
            example = "Password@123")
    @NotBlank(message = "Password is required")
    private String password;

    // =========================
    // PHONE
    // =========================
    @Schema(
            description = "Mobile number",
            example = "9000824146")
    private String phone;
}