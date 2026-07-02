
package com.example.rental_platform.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ChatMessage {

    // =========================
    // CHAT MESSAGE ID
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // BOOKING
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // =========================
    // SENDER
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // =========================
    // MESSAGE
    // =========================
    @Column(nullable = false, length = 1000)
    private String message;

    // =========================
    // SENT AT
    // =========================
    @Column(nullable = false)
    private LocalDateTime sentAt;

    // =========================
    // READ STATUS
    // =========================
    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;

    // =========================
    // READ TIMESTAMP
    // =========================
    private LocalDateTime readAt;

}