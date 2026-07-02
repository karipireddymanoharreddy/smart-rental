
package com.example.rental_platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    // =========================
    // EMAIL SERVICE
    // =========================
    private final EmailService emailService;

    // =========================
    // SEND TEST EMAIL
    // =========================
    @GetMapping("/test")
    public String sendTestEmail() {

        emailService.sendSimpleEmail(
                "YOUR_GMAIL@gmail.com",
                "Rental Platform - Test Email",
                """
                Congratulations!

                Your Spring Boot email configuration is working successfully.

                This email was sent from your Rental Platform project.

                Regards,
                Rental Platform Team
                """);

        return "Test email sent successfully.";
    }

}