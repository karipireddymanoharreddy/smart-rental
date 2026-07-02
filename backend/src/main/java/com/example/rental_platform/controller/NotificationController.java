
package com.example.rental_platform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.NotificationResponse;
import com.example.rental_platform.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    // =========================
    // SERVICE
    // =========================
    private final NotificationService notificationService;

    // =========================
    // GET MY NOTIFICATIONS
    // =========================
    @GetMapping
    public ApiResponse<List<NotificationResponse>> getMyNotifications() {

        return notificationService.getMyNotifications();
    }

    // =========================
    // MARK NOTIFICATION AS READ
    // =========================
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<String> markAsRead(
            @PathVariable Long notificationId) {

        return notificationService.markAsRead(notificationId);
    }

    // =========================
    // GET UNREAD COUNT
    // =========================
    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount() {

        return notificationService.getUnreadCount();
    }


    // =========================
// MARK ALL NOTIFICATIONS AS READ
// =========================
@PatchMapping("/read-all")
public ApiResponse<String> markAllAsRead() {

    return notificationService.markAllAsRead();
}


}