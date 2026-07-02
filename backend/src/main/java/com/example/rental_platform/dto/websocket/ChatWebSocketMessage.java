
package com.example.rental_platform.dto.websocket;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatWebSocketMessage {

    private Long id;

    private Long bookingId;

    private String senderName;

    private String message;

    private LocalDateTime sentAt;

    private boolean isRead;
}