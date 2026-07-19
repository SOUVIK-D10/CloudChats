package com.sopvlight.cloudchat_backend.Message.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopvlight.cloudchat_backend.Exception.GeneralException;
import com.sopvlight.cloudchat_backend.Message.DTO.SendMessageDTO;
import com.sopvlight.cloudchat_backend.Message.Service.MessageService;
import com.sopvlight.cloudchat_backend.Security.Auth.Model.UserData;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService){
        this.messageService=messageService;
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal UserData sender, @RequestBody SendMessageDTO dto ) throws GeneralException{
        messageService.sendMessage(dto.threadId(), dto.content(), sender.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
