
package com.example.rental_platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
        implements WebSocketMessageBrokerConfigurer {

    // =========================
    // REGISTER STOMP ENDPOINT
    // =========================
    @Override
    public void registerStompEndpoints(
            StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    // =========================
    // CONFIGURE MESSAGE BROKER
    // =========================
    @Override
    public void configureMessageBroker(
            MessageBrokerRegistry registry) {

        // Messages sent TO clients
        registry.enableSimpleBroker("/topic");

        // Messages sent FROM clients
        registry.setApplicationDestinationPrefixes("/app");
    }
}