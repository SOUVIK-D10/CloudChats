package com.sopvlight.cloudchat_backend.Message.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopvlight.cloudchat_backend.Exception.GeneralException;
import com.sopvlight.cloudchat_backend.Message.DTO.SendMessageDTO;
import com.sopvlight.cloudchat_backend.Message.Service.MessageService;
import com.sopvlight.cloudchat_backend.Security.Auth.Model.UserData;
import com.sopvlight.cloudchat_backend.Threads.Controller.ThreadController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService messageService;
    private final Logger log;
    @Autowired
    public MessageController(MessageService messageService){
        this.messageService=messageService;
        this.log = LoggerFactory.getLogger(ThreadController.class);
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal UserData sender, @RequestBody SendMessageDTO dto ) throws GeneralException{
        messageService.sendMessage(dto.threadId(), dto.content(), sender.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editMessage(@AuthenticationPrincipal UserData sender, @PathVariable Long id,@RequestBody SendMessageDTO dto ) throws GeneralException{
        messageService.editMessage(sender.getUserId(),id,dto.threadId(),dto.content());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/delete/{threadId}/{id}")
    public ResponseEntity<?> deleteMessage(@AuthenticationPrincipal UserData sender, @PathVariable Long threadId,@PathVariable Long id) throws GeneralException{
        messageService.deleteMessage(sender.getUserId(),threadId,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/pin-toggle/{threadId}/{id}")
    public ResponseEntity<?> pinToggle(@AuthenticationPrincipal UserData sender, @PathVariable Long threadId,@PathVariable Long id) throws GeneralException{
        messageService.pinToggle(sender.getUserId(), threadId,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
