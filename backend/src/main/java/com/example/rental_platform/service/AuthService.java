
package com.example.rental_platform.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.request.ForgotPasswordRequest;
import com.example.rental_platform.dto.request.LoginRequest;
import com.example.rental_platform.dto.request.RegisterRequest;
import com.example.rental_platform.dto.request.ResetPasswordRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.exception.EmailAlreadyExistsException;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.UserRepository;
import com.example.rental_platform.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    // =========================
    // DEPENDENCIES
    // =========================
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    // =========================
    // REGISTER USER
    // =========================
    public ApiResponse<Object> register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .build();

        // Save User
        userRepository.save(user);

        // =========================
        // SEND WELCOME EMAIL
        // =========================
        emailService.sendSimpleEmail(
                user.getEmail(),

                "Welcome to Rental Platform",

                """
                Hello %s,

                Welcome to Rental Platform!

                Your account has been created successfully.

                You can now:

                • Browse rental items
                • List your own items
                • Book items securely
                • Chat with owners
                • Receive notifications

                Thank you for joining us.

                Regards,
                Rental Platform Team
                """.formatted(user.getName()));

        return ApiResponse.builder()
                .success(true)
                .message("User Registered Successfully")
                .data(null)
                .build();
    }

    // =========================
    // LOGIN USER
    // =========================
    public ApiResponse<String> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Login Successful")
                .data(token)
                .build();
    }

    // =========================
    // FORGOT PASSWORD
    // =========================
    public ApiResponse<Object> forgotPassword(
            ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));

        // Generate secure token
        String token = UUID.randomUUID().toString();

        // Save token
        user.setResetPasswordToken(token);

        // Token expires in 15 minutes
        user.setResetPasswordTokenExpiry(
                LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        // =========================
        // SEND RESET PASSWORD EMAIL
        // =========================
        emailService.sendSimpleEmail(
                user.getEmail(),

                "Reset Your Password",

                """
                Hello %s,

                We received a request to reset your password.

                Use the following reset token:

                %s

                This token will expire in 15 minutes.

                If you did not request this password reset, you can safely ignore this email.

                Regards,
                Rental Platform Team
                """.formatted(
                        user.getName(),
                        token));

        return ApiResponse.builder()
                .success(true)
                .message("Password reset instructions have been sent to your email.")
                .data(null)
                .build();
    }

    // =========================
    // RESET PASSWORD
    // =========================
    public ApiResponse<Object> resetPassword(
            ResetPasswordRequest request) {

        User user = userRepository
                .findByResetPasswordToken(request.getToken())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Invalid reset token"));

        // Check token expiry
        if (user.getResetPasswordTokenExpiry()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Reset token has expired");
        }

        // Update password
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()));

        // Clear token
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        userRepository.save(user);

        return ApiResponse.builder()
                .success(true)
                .message("Password reset successfully.")
                .data(null)
                .build();
    }
}