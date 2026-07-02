
package com.example.rental_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rental_platform.model.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // =========================
    // GET CHAT MESSAGES BY BOOKING
    // =========================
    List<ChatMessage> findByBookingIdOrderBySentAtAsc(Long bookingId);

    // =========================
    // GET UNREAD MESSAGES
    // =========================
    List<ChatMessage> findByBookingIdAndIsReadFalse(Long bookingId);

}