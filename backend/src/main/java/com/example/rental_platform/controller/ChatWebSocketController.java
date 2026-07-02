
package com.example.rental_platform.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatWebSocketController {

    // =========================
    // WEBSOCKET CONNECTION TEST
    // =========================
    @MessageMapping("/chat.test")
    public void testConnection() {

        log.info("WebSocket message received successfully.");
    }
}