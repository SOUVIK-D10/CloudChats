package com.sopvlight.cloudchat_backend.Threads.DTO;

import java.util.Set;

import jakarta.validation.constraints.Size;

public record NewGroupThreadRequestDTO(
    String name,
    @Size(min = 2, message = "A group thread must have at least 2 participants")
    Set<String> usernames
) {
    
}
