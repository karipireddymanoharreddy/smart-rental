
package com.example.rental_platform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.request.SendMessageRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.ChatMessageResponse;
import com.example.rental_platform.dto.websocket.ChatWebSocketMessage;
import com.example.rental_platform.exception.BookingNotFoundException;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.Booking;
import com.example.rental_platform.model.ChatMessage;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.BookingRepository;
import com.example.rental_platform.repository.ChatMessageRepository;
import com.example.rental_platform.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    // =========================
    // REPOSITORIES
    // =========================
    private final ChatMessageRepository chatMessageRepository;

    private final BookingRepository bookingRepository;

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
// GET BOOKING
// =========================
private Booking getBooking(Long bookingId) {

    return bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                    new BookingNotFoundException(
                            "Booking not found"));
}


// =========================
// VALIDATE CHAT PARTICIPANT
// =========================
private void validateChatParticipant(
        Booking booking,
        User user) {

    boolean isOwner = booking.getRentalItem()
            .getOwner()
            .getId()
            .equals(user.getId());

    boolean isRenter = booking.getRenter()
            .getId()
            .equals(user.getId());

    if (!isOwner && !isRenter) {

        throw new IllegalArgumentException(
                "You are not allowed to access this conversation");
    }
}





// =========================
// SEND MESSAGE
// =========================
public ApiResponse<ChatMessageResponse> sendMessage(
        Long bookingId,
        SendMessageRequest request) {

    // Get logged-in user
    User user = getLoggedInUser();

    // Get booking
    Booking booking = getBooking(bookingId);

    // Validate participant
    validateChatParticipant(booking, user);

    // Create chat message
    ChatMessage chatMessage = ChatMessage.builder()
            .booking(booking)
            .sender(user)
            .message(request.getMessage())
            .sentAt(LocalDateTime.now())
            .build();

    // Save message
    chatMessage = chatMessageRepository.save(chatMessage);

    // Convert Entity -> DTO
    ChatMessageResponse response = ChatMessageResponse.builder()
            .id(chatMessage.getId())
            .senderName(chatMessage.getSender().getName())
            .message(chatMessage.getMessage())
            .sentAt(chatMessage.getSentAt())
            .isRead(chatMessage.isRead())
            .readAt(chatMessage.getReadAt())
            .build();

    // Create WebSocket message
    ChatWebSocketMessage wsMessage = ChatWebSocketMessage.builder()
            .id(chatMessage.getId())
            .bookingId(bookingId)
            .senderName(chatMessage.getSender().getName())
            .message(chatMessage.getMessage())
            .sentAt(chatMessage.getSentAt())
            .isRead(chatMessage.isRead())
            .build();

    // Broadcast message to all subscribed clients
    messagingTemplate.convertAndSend(
            "/topic/bookings/" + bookingId,
            wsMessage);

    return ApiResponse.<ChatMessageResponse>builder()
            .success(true)
            .message("Message sent successfully")
            .data(response)
            .build();
}





// =========================
// GET CONVERSATION
// =========================
public ApiResponse<List<ChatMessageResponse>> getConversation(
        Long bookingId) {

    // Get logged-in user
    User user = getLoggedInUser();

    // Get booking
    Booking booking = getBooking(bookingId);

    // Validate participant
    validateChatParticipant(booking, user);

    // =========================
    // MARK MESSAGES AS READ
    // =========================
    List<ChatMessage> unreadMessages =
            chatMessageRepository.findByBookingIdAndIsReadFalse(
                    bookingId);

    for (ChatMessage message : unreadMessages) {

        // Only mark messages sent by the other participant
        if (!message.getSender().getId().equals(user.getId())) {

            message.setRead(true);
            message.setReadAt(LocalDateTime.now());

            chatMessageRepository.save(message);
        }
    }

    // Get chat messages
    List<ChatMessage> chatMessages =
            chatMessageRepository.findByBookingIdOrderBySentAtAsc(
                    bookingId);

    // Convert Entity -> DTO
    List<ChatMessageResponse> responses = chatMessages.stream()
            .map(chatMessage -> ChatMessageResponse.builder()
                    .id(chatMessage.getId())
                    .senderName(chatMessage.getSender().getName())
                    .message(chatMessage.getMessage())
                    .sentAt(chatMessage.getSentAt())
                    .isRead(chatMessage.isRead())
                    .readAt(chatMessage.getReadAt())
                    .build())
            .collect(Collectors.toList());

    return ApiResponse.<List<ChatMessageResponse>>builder()
            .success(true)
            .message("Conversation fetched successfully")
            .data(responses)
            .build();
}




}