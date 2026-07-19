package com.sopvlight.cloudchat_backend.Message.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopvlight.cloudchat_backend.Message.Entity.Message;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long>{

    List<Message> findAllByThreadId(Long threadId);
    
}
