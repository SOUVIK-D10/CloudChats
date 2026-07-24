package com.sopvlight.cloudchat_backend.Threads.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopvlight.cloudchat_backend.Threads.Constants.ThreadConstant;
import com.sopvlight.cloudchat_backend.Threads.DTO.OpenedThreadDTO;
import com.sopvlight.cloudchat_backend.Threads.DTO.ThreadListItemDTO;
import com.sopvlight.cloudchat_backend.Threads.Entity.Thread;
import com.sopvlight.cloudchat_backend.Exception.GeneralException;
import com.sopvlight.cloudchat_backend.Message.DTO.MessageDTO;

import com.sopvlight.cloudchat_backend.Message.Service.MessageService;
import com.sopvlight.cloudchat_backend.Threads.Repo.ThreadRepo;
import com.sopvlight.cloudchat_backend.Users.Service.UserService;

import jakarta.transaction.Transactional;




@Service
public class ThreadService {
    private ThreadRepo threadRepo;
    private UserService userService;
    private MessageService messageService;
    @Autowired
    public ThreadService(ThreadRepo threadRepo, UserService userService,MessageService messageService) {
        this.threadRepo = threadRepo;
        this.userService = userService;
        this.messageService = messageService;
    }
    public Long createPrivateThread(int userId, String username) throws GeneralException {
        int otherUserId =  userService.getUserIdByUsername(username);
        Set<Integer>  participantsId = new HashSet<>();
        participantsId.add(userId);
        participantsId.add(otherUserId);
        Thread thread = new Thread(participantsId, "Private Chat", ThreadConstant.THREAD_TYPE_PRIVATE, userId, participantsId);
        thread = threadRepo.save(thread);
        return thread.getId();
    }
    public List<ThreadListItemDTO> getAllThreads(int userId,String type) {
        List<Thread> list = threadRepo.findAllForMy(userId,type);
        List<ThreadListItemDTO> listDTO = new ArrayList<>();
        for(Thread t:list){
            listDTO.add(new ThreadListItemDTO(t.getId(), t.getThreadName(), t.getThreadType()));
        }
        return listDTO;
    }
    public OpenedThreadDTO getThreadById(int userId, Long id) throws GeneralException {
        Thread thread = threadRepo.findByIdAndParticipantId(id, userId)
        .orElseThrow(() -> new GeneralException("400:Thread with ID " + id + " not found or you are not a participant."));
        List<MessageDTO> list = messageService.getMessageHistory(id);
        Set<String> usernames = new HashSet<>();
        for(int i:thread.getParticipantIds()){
            usernames.add(userService.getUsernameByUserId(i));
        }
        OpenedThreadDTO dto = new OpenedThreadDTO(id,thread.getThreadName(), thread.getThreadType(),usernames,list);
        return dto;
    }
    public OpenedThreadDTO createGroupThread(
            int userId,
            Set<String> usernames,
            String name) throws GeneralException {
        Set<Integer> participantIds = userService.getUserIdsByUsernames(usernames);
        participantIds.add(userId);
        Thread group = new Thread(participantIds, name, ThreadConstant.THREAD_TYPE_GROUP, userId, participantIds);
        group = threadRepo.save(group);
        return new OpenedThreadDTO(
                                    group.getId(),
                                    group.getThreadName(),
                                    group.getThreadType(),
                                    userService.getUsernameByUserId(group.getParticipantIds()),
                                    messageService.getMessageHistory(group.getId())
                                );
    }
    public OpenedThreadDTO createChannelThread(
            int userId,
            // Set<String> usernames,
            String name) throws GeneralException {
        // Set<Integer> allowList = userService.getUserIdsByUsernames(usernames);
        // allowList.add(userId);
        Set<Integer> list = new HashSet<>();
        list.add(userId);
        Thread channel = new Thread(list, name, ThreadConstant.THREAD_TYPE_CHANNEL, userId,list);
        channel = threadRepo.save(channel);
        return new OpenedThreadDTO(
                                    channel.getId(),
                                    channel.getThreadName(),
                                    channel.getThreadType(),
                                    null,
                                    messageService.getMessageHistory(channel.getId())
                                );
    }
    public void validateMessagingPermissions(int senderId, Long threadId) throws GeneralException {
        Thread thread = threadRepo.findByIdAndParticipantId(threadId, senderId).orElseThrow(() ->
            new GeneralException("400: You are either not a participant or This thread does not exists")
        );
        if(thread.getMessageingAllowedList().contains(senderId)) throw new GeneralException("401:You are Not permitted to send messages in this thread");
    }
    @Transactional
    public void addMember(Long threadId, String username, int userId) throws GeneralException {
        int newParticipantId = userService.getUserIdByUsername(username);
        Thread thread = threadRepo.findByIdAndParticipantId(threadId, userId).orElseThrow(() ->
            new GeneralException("400: You are either not a participant or This thread does not exists"));
        if(thread.getThreadType().equals(ThreadConstant.THREAD_TYPE_PRIVATE)) throw new GeneralException("400: Action not possible");
        if(thread.getThreadType().equals(ThreadConstant.THREAD_TYPE_GROUP) && thread.getMessageingAllowedList().contains(userId)) throw new GeneralException("401: Action not permitted");
        thread.addParticipantIds(newParticipantId);
    }

}
