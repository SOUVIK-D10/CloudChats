package com.sopvlight.cloudchat_backend.WebSockets.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // This is the URL the frontend will connect to: ws://localhost:8080/ws-chat
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*") // Allows cross-origin requests from your frontend
                .withSockJS(); // Fallback for browsers that don't support WebSockets natively
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // The prefix for outbound messages (Backend -> Frontend)
        registry.enableSimpleBroker("/topic");
        
        // The prefix for inbound messages (Frontend -> Backend via WebSocket, if you use it later)
        registry.setApplicationDestinationPrefixes("/app");
    }
}