package com.sopvlight.cloudchat_backend.Threads.Entity;

import java.time.LocalDateTime;
// import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cloudchat_message_threads")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private Set<Integer> participantIds;
    private int creatorId;
    private String threadName;
    private String threadType;
    @ElementCollection
    private Set<Integer> messageingAllowedList;
    private LocalDateTime createdAt;

    public Thread() {
    }
    public Thread(Set<Integer> participantIds, String threadName, String threadType, int creatorId,Set<Integer> messageingAllowedList){
        this.participantIds = participantIds;
        this.threadName = threadName;
        this.threadType = threadType;
        this.creatorId = creatorId;
        this.createdAt = LocalDateTime.now();
        this.messageingAllowedList=messageingAllowedList;

    }
    public int getCreatorId() {
        return creatorId;
    }
    public Set<Integer> getMessageingAllowedList() {
        return messageingAllowedList;
    }
    public void setMessageingAllowedList(Set<Integer> messageingAllowedList) {
        this.messageingAllowedList = messageingAllowedList;
    }
    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Set<Integer> getParticipantIds() {
        return participantIds;
    }
    public void setParticipantIds(Set<Integer> participantIds) {
        this.participantIds = participantIds;
    }
    public void addParticipantIds(Integer participantId) {
        this.participantIds.add(participantId);
    }
    public String getThreadName() {
        return threadName;
    }
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
    public String getThreadType() {
        return threadType;
    }
    public void setThreadType(String threadType) {
        this.threadType = threadType;
    }
    // public Set<Long> getMessageIds() {
    //     return messageIds;
    // }
    // public void setMessageIds(Set<Long> messageIds) {
    //     this.messageIds = messageIds;
    // }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
