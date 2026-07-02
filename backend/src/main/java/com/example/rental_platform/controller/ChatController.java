
package com.example.rental_platform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.request.SendMessageRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.ChatMessageResponse;
import com.example.rental_platform.service.ChatService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    // =========================
    // CHAT SERVICE
    // =========================
    private final ChatService chatService;



// =========================
// SEND MESSAGE
// =========================
@PostMapping("/bookings/{bookingId}/messages")
public ApiResponse<ChatMessageResponse> sendMessage(

        @PathVariable Long bookingId,

        @Valid @RequestBody SendMessageRequest request) {

    return chatService.sendMessage(
            bookingId,
            request);
}



// =========================
// GET CONVERSATION
// =========================
@GetMapping("/bookings/{bookingId}/messages")
public ApiResponse<List<ChatMessageResponse>> getConversation(

        @PathVariable Long bookingId) {

    return chatService.getConversation(bookingId);
}


}