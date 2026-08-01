package com.sopvlight.cloudchat_backend.Message.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cloudchat_messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long threadId;
    private String content;
    private int senderId;
    private LocalDateTime timestamp;
    private LocalDateTime originalTimestamp;
    private boolean isEdited;
    private boolean isDeleted;
    private boolean isPinned;
    private boolean isRead;
    private boolean isStarred;

    public Message(){}
    public Message(Long threadId, String content, int senderId) {
        this.threadId = threadId;
        this.content = content;
        this.senderId = senderId;
        this.timestamp = LocalDateTime.now();
        this.originalTimestamp = LocalDateTime.now();
        this.isEdited = false;
        this.isDeleted = false;
        this.isPinned = false;
        this.isRead = false;
        this.isStarred = false;
    }

    public Long getId() {
        return id;
    }

    public Long getThreadId() {
        return threadId;
    }

    public String getContent() {
        return content;
    }

    public int getSenderId() {
        return senderId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getOriginalTimestamp() {
        return originalTimestamp;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void putTimestamp() {
        this.timestamp = LocalDateTime.now();
    }

    public void edited() {
        this.isEdited = true;
        this.timestamp = LocalDateTime.now();
    }

    public void deleted() {
        this.isDeleted = true;
    }

    public void togglePin() {
        this.isPinned = !this.isPinned;
    }

    public void read() {
        this.isRead = true;
    }

    public void toggleStarred() {
        this.isStarred = !this.isStarred;
    }

}
