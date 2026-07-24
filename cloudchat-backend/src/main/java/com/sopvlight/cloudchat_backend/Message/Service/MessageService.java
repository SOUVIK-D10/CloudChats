package com.sopvlight.cloudchat_backend.Message.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sopvlight.cloudchat_backend.Exception.GeneralException;
import com.sopvlight.cloudchat_backend.Message.DTO.MessageDTO;
import com.sopvlight.cloudchat_backend.Message.Entity.Message;
import com.sopvlight.cloudchat_backend.Message.Repo.MessageRepo;
import com.sopvlight.cloudchat_backend.Threads.Service.ThreadService;
import com.sopvlight.cloudchat_backend.Users.Service.UserService;
import com.sopvlight.cloudchat_backend.WebSockets.Service.BroadcastService;

@Service
public class MessageService {
    private final MessageRepo messageRepo;
    private UserService userService;
    private BroadcastService broadcastService;
    private ApplicationContext context;
    @Autowired
    public MessageService(MessageRepo messageRepo, UserService userService,BroadcastService broadcastService, ApplicationContext context) {
        this.messageRepo = messageRepo;
        this.userService=userService;
        this.broadcastService=broadcastService;
        this.context=context;
    }
    private MessageDTO saveMessage(Long threadId, String content, int senderId) throws GeneralException{
        Message message = new Message(threadId, content, senderId);
        message = messageRepo.save(message);
        return   new MessageDTO(message.getId(), 
                                threadId,
                                message.getContent(),
                                userService.getUsernameByUserId(senderId),
                                message.getTimestamp(),
                                message.isEdited(),
                                message.isDeleted(),
                                message.isPinned(),
                                message.isRead(),
                                message.isStarred());
    }
    public void sendMessage(Long threadId, String content, int senderId) throws GeneralException{
        ThreadService service = context.getBean(ThreadService.class);
        service.validateMessagingPermissions(senderId,threadId);
        MessageDTO dto = saveMessage(threadId, content, senderId);
        broadcastService.sendMessage(threadId, dto );
    }
    public List<MessageDTO> getMessageHistory(Long threadId) throws GeneralException{
        List<Message> list = messageRepo.findAllByThreadId(threadId);
        List<MessageDTO> listDTO = new ArrayList<>();
        for( Message m:list){
            listDTO.add(new MessageDTO(m.getId(), 
                                        threadId,
                                        m.getContent(),
                                        userService.getUsernameByUserId(m.getSenderId()),
                                        m.getTimestamp(),
                                        m.isEdited(),
                                        m.isDeleted(),
                                        m.isPinned(),
                                        m.isRead(),
                                        m.isStarred()));
        }
        return listDTO;
    }
}
