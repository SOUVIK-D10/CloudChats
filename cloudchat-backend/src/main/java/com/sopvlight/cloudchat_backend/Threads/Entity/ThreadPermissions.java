package com.sopvlight.cloudchat_backend.Threads.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "thread_permissions")
public class ThreadPermissions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int userId;
    private Long threadId;
    private boolean canSendMessage;
    private boolean canChangeThreadName;    
    private boolean canAddMembers;    
    private boolean canInviteMembers;    
    private boolean canRemoveMembers;
    private boolean canChangeThreadDescription; 
    private boolean canPinMessages;
    private boolean canForceDeleteMessages;
    public ThreadPermissions(Long id, int userId, Long threadId, boolean canSendMessage, boolean canChangeThreadName,
            boolean canAddMembers, boolean canInviteMembers, boolean canRemoveMembers,
            boolean canChangeThreadDescription, boolean canPinMessages, boolean canForceDeleteMessages) {
        this.id = id;
        this.userId = userId;
        this.threadId = threadId;
        this.canSendMessage = canSendMessage;
        this.canChangeThreadName = canChangeThreadName;
        this.canAddMembers = canAddMembers;
        this.canInviteMembers = canInviteMembers;
        this.canRemoveMembers = canRemoveMembers;
        this.canChangeThreadDescription = canChangeThreadDescription;
        this.canPinMessages = canPinMessages;
        this.canForceDeleteMessages = canForceDeleteMessages;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Long getThreadId() {
        return threadId;
    }
    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }
    public boolean isCanSendMessage() {
        return canSendMessage;
    }
    public void setCanSendMessage(boolean canSendMessage) {
        this.canSendMessage = canSendMessage;
    }
    public boolean isCanChangeThreadName() {
        return canChangeThreadName;
    }
    public void setCanChangeThreadName(boolean canChangeThreadName) {
        this.canChangeThreadName = canChangeThreadName;
    }
    public boolean isCanAddMembers() {
        return canAddMembers;
    }
    public void setCanAddMembers(boolean canAddMembers) {
        this.canAddMembers = canAddMembers;
    }
    public boolean isCanInviteMembers() {
        return canInviteMembers;
    }
    public void setCanInviteMembers(boolean canInviteMembers) {
        this.canInviteMembers = canInviteMembers;
    }
    public boolean isCanRemoveMembers() {
        return canRemoveMembers;
    }
    public void setCanRemoveMembers(boolean canRemoveMembers) {
        this.canRemoveMembers = canRemoveMembers;
    }
    public boolean isCanChangeThreadDescription() {
        return canChangeThreadDescription;
    }
    public void setCanChangeThreadDescription(boolean canChangeThreadDescription) {
        this.canChangeThreadDescription = canChangeThreadDescription;
    }
    public boolean isCanPinMessages() {
        return canPinMessages;
    }
    public void setCanPinMessages(boolean canPinMessages) {
        this.canPinMessages = canPinMessages;
    }
    public boolean isCanForceDeleteMessages() {
        return canForceDeleteMessages;
    }
    public void setCanForceDeleteMessages(boolean canForceDeleteMessages) {
        this.canForceDeleteMessages = canForceDeleteMessages;
    }

}
