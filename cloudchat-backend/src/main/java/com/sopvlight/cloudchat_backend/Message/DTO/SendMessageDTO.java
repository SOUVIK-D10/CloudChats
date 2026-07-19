package com.sopvlight.cloudchat_backend.Message.DTO;

public record SendMessageDTO(
    Long threadId,
    String content
) {
    
}
