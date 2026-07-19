package com.sopvlight.cloudchat_backend.Threads.DTO;

import java.util.List;
import java.util.Set;

import com.sopvlight.cloudchat_backend.Message.DTO.MessageDTO;

public record OpenedThreadDTO(
    Long id,
    String name,
    String type,
    Set<String> participant_usernames,
    List<MessageDTO> messages
) {

}
