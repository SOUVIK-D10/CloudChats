package com.sopvlight.cloudchat_backend.Message.DTO;

import java.time.LocalDateTime;

public record MessageDTO(
        Long id,
        Long threadId,
        String content,
        String sender_username,
        LocalDateTime timestamp,
        boolean isEdited,
        // boolean isDeleted,
        boolean isPinned,
        boolean isRead,
        boolean isStarred) {

}
