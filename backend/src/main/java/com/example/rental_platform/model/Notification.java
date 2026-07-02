
package com.example.rental_platform.model;

import java.time.LocalDateTime;

import com.example.rental_platform.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    // =========================
    // NOTIFICATION ID
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // RECIPIENT
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    // =========================
    // NOTIFICATION TYPE
    // =========================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // =========================
    // TITLE
    // =========================
    @Column(nullable = false, length = 150)
    private String title;

    // =========================
    // MESSAGE
    // =========================
    @Column(nullable = false, length = 500)
    private String message;

    // =========================
    // READ STATUS
    // =========================
    @Builder.Default
    @Column(nullable = false)
    private boolean isRead = false;

    // =========================
    // CREATED TIME
    // =========================
    @Column(nullable = false)
    private LocalDateTime createdAt;
}