
package com.example.rental_platform.dto.response;

import java.time.LocalDateTime;

import com.example.rental_platform.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    // =========================
    // NOTIFICATION ID
    // =========================
    private Long id;

    // =========================
    // TYPE
    // =========================
    private NotificationType type;

    // =========================
    // TITLE
    // =========================
    private String title;

    // =========================
    // MESSAGE
    // =========================
    private String message;

    // =========================
    // READ STATUS
    // =========================
    private boolean isRead;

    // =========================
    // CREATED TIME
    // =========================
    private LocalDateTime createdAt;

}