
package com.example.rental_platform.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.request.ForgotPasswordRequest;
import com.example.rental_platform.dto.request.LoginRequest;
import com.example.rental_platform.dto.request.RegisterRequest;
import com.example.rental_platform.dto.request.ResetPasswordRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
    name = "Authentication",
    description = "APIs for user authentication and password management"
)
public class AuthController {

    // =========================
    // SERVICE
    // =========================
    private final AuthService authService;

    // =========================
    // REGISTER USER
    // =========================
    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ApiResponse<Object> register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    // =========================
    // LOGIN USER
    // =========================
    @Operation(summary = "Login and generate JWT token")
    @PostMapping("/login")
    public ApiResponse<String> login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }

    // =========================
    // FORGOT PASSWORD
    // =========================
    @Operation(summary = "Send password reset email")
    @PostMapping("/forgot-password")
    public ApiResponse<Object> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        return authService.forgotPassword(request);
    }

    // =========================
    // RESET PASSWORD
    // =========================
    @Operation(summary = "Reset user password using reset token")
    @PostMapping("/reset-password")
    public ApiResponse<Object> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        return authService.resetPassword(request);
    }
}