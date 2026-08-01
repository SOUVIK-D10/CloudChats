package com.sopvlight.cloudchat_backend.Message.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import jakarta.transaction.Transactional;

@Service
public class MessageService {
    private final Logger log = LoggerFactory.getLogger(MessageService.class);
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
    @Transactional
    private MessageDTO saveMessage(Long threadId, String content, int senderId) throws GeneralException{
        Message message = new Message(threadId, content, senderId);
        message = messageRepo.save(message);
        return mapMessageToMessageDTO(message);
    }
    private MessageDTO mapMessageToMessageDTO(Message m) throws GeneralException{
        return new MessageDTO(m.getId(), 
                                        m.getThreadId(),
                                        m.getContent(),
                                        userService.getUsernameByUserId(m.getSenderId()),
                                        m.getTimestamp(),
                                        m.isEdited(),
                                        m.isPinned(),
                                        m.isRead(),
                                        m.isStarred());
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
            listDTO.add(mapMessageToMessageDTO(m));
        }
        return listDTO;
    }
    @Transactional
    public void editMessage(int userId, Long id, Long threadId, String content) throws GeneralException {
        Message msg = messageRepo.findByIdAndThreadIdAndSenderIdAndIsDeletedFalse(id,threadId,userId)
                                    .orElseThrow(()->new GeneralException("400:Either requested message is invalid or you don't have sufficient permissions"));
        msg.edited();
        msg.setContent(content);
        messageRepo.save(msg);
        MessageDTO dto = mapMessageToMessageDTO(msg);
        broadcastService.sendEditedMessage(threadId, dto);
    }
    public void deleteMessage(int userId, Long threadId, Long id) throws GeneralException {
        int ok = messageRepo.softDeleteById(id, threadId, userId); 
        if(ok < 1) throw new GeneralException("400:Either requested message is invalid or you don't have sufficient permissions");
    }
    public void pinToggle(int userId, Long threadId, Long id) throws GeneralException {
        int ok = messageRepo.togglePin(id, threadId, userId); 
        if(ok < 1) throw new GeneralException("400:Either requested message is invalid or you don't have sufficient permissions");
    }
}
