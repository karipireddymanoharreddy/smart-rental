
package com.example.rental_platform.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

    // =========================
    // MESSAGE ID
    // =========================
    private Long id;

    // =========================
    // SENDER NAME
    // =========================
    private String senderName;

    // =========================
    // MESSAGE
    // =========================
    private String message;

    // =========================
    // SENT TIME
    // =========================
    private LocalDateTime sentAt;

    // =========================
    // READ STATUS
    // =========================
    private boolean isRead;

    // =========================
    // READ TIMESTAMP
    // =========================
    private LocalDateTime readAt;

}