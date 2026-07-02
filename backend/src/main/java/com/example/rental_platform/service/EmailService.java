
package com.example.rental_platform.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    // =========================
    // JAVA MAIL SENDER
    // =========================
    private final JavaMailSender mailSender;

    // =========================
    // SEND SIMPLE EMAIL
    // =========================
    public void sendSimpleEmail(
            String to,
            String subject,
            String body) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}