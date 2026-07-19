package com.sopvlight.cloudchat_backend.WebSockets.Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sopvlight.cloudchat_backend.Message.DTO.MessageDTO;

@Service
public class BroadcastService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public void sendMessage(Long threadId, MessageDTO dto) {
        
        // 1. Save the message to PostgreSQL via Spring Data JPA
        // Message savedMessage = messageService.saveMessage(request);
        
        // 2. Define the exact WebSocket topic channel for this specific thread
        String destination = "/topic/thread/" + threadId;

        // 3. Broadcast the saved payload to anyone subscribed to that topic
        messagingTemplate.convertAndSend(destination, dto);
    }
}
