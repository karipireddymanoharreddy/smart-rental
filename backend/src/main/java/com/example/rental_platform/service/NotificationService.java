
package com.example.rental_platform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.NotificationResponse;
import com.example.rental_platform.dto.websocket.NotificationWebSocketMessage;
import com.example.rental_platform.enums.NotificationType;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.Notification;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.NotificationRepository;
import com.example.rental_platform.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    // =========================
    // REPOSITORY
    // =========================
    private final NotificationRepository notificationRepository;

    // =========================
    // USER REPOSITORY
    // =========================
    private final UserRepository userRepository;

    // =========================
    // WEBSOCKET MESSAGING
    // =========================
    private final SimpMessagingTemplate messagingTemplate;

    // =========================
    // GET LOGGED-IN USER
    // =========================
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));
    }

    // =========================
    // CREATE NOTIFICATION
    // =========================
    public void createNotification(
            User recipient,
            NotificationType type,
            String title,
            String message) {

        // Create notification
        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(type)
                .title(title)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        // Save notification
        notification = notificationRepository.save(notification);

        // Create WebSocket DTO
        NotificationWebSocketMessage wsMessage =
                NotificationWebSocketMessage.builder()
                        .id(notification.getId())
                        .type(notification.getType())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.isRead())
                        .build();

        // Broadcast notification
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + recipient.getId(),
                wsMessage);
    }

    // =========================
    // GET MY NOTIFICATIONS
    // =========================
    public ApiResponse<List<NotificationResponse>> getMyNotifications() {

        User user = getLoggedInUser();

        List<NotificationResponse> notifications =
                notificationRepository
                        .findByRecipientIdOrderByCreatedAtDesc(user.getId())
                        .stream()
                        .map(notification -> NotificationResponse.builder()
                                .id(notification.getId())
                                .type(notification.getType())
                                .title(notification.getTitle())
                                .message(notification.getMessage())
                                .isRead(notification.isRead())
                                .createdAt(notification.getCreatedAt())
                                .build())
                        .collect(Collectors.toList());

        return ApiResponse.<List<NotificationResponse>>builder()
                .success(true)
                .message("Notifications fetched successfully")
                .data(notifications)
                .build();
    }

    // =========================
    // MARK NOTIFICATION AS READ
    // =========================
    public ApiResponse<String> markAsRead(Long notificationId) {

        User user = getLoggedInUser();

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Notification not found"));

        // Ensure notification belongs to logged-in user
        if (!notification.getRecipient().getId().equals(user.getId())) {

            throw new IllegalArgumentException(
                    "You are not allowed to access this notification");
        }

        notification.setRead(true);

        notificationRepository.save(notification);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Notification marked as read")
                .data(null)
                .build();
    }

    // =========================
    // GET UNREAD COUNT
    // =========================
    public ApiResponse<Long> getUnreadCount() {

        User user = getLoggedInUser();

        long unreadCount =
                notificationRepository
                        .countByRecipientIdAndIsReadFalse(
                                user.getId());

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Unread notification count fetched successfully")
                .data(unreadCount)
                .build();
    }

    // =========================
    // MARK ALL NOTIFICATIONS AS READ
    // =========================
    public ApiResponse<String> markAllAsRead() {

        User user = getLoggedInUser();

        int updatedRows =
                notificationRepository.markAllAsRead(
                        user.getId());

        return ApiResponse.<String>builder()
                .success(true)
                .message(updatedRows + " notification(s) marked as read")
                .data(null)
                .build();
    }

}