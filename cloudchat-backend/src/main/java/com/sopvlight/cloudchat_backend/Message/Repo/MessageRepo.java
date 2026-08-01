package com.sopvlight.cloudchat_backend.Message.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopvlight.cloudchat_backend.Message.Entity.Message;

import jakarta.transaction.Transactional;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.threadId = :threadId AND m.isDeleted = FALSE")
    List<Message> findAllByThreadId(@Param("threadId") Long threadId);

    Optional<Message> findByIdAndThreadIdAndSenderIdAndIsDeletedFalse(Long id, Long threadId, int senderId);
    
    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.isDeleted = TRUE WHERE m.id = :id AND m.threadId = :threadId AND m.senderId = :userId")
    int softDeleteById(@Param("id") Long id, @Param("threadId") Long threadId, @Param("userId") int userId); // Changed boolean to int

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.isPinned = CASE WHEN m.isPinned = TRUE THEN FALSE ELSE TRUE END WHERE m.id = :id AND m.threadId = :threadId AND m.senderId = :userId AND m.isDeleted = FALSE")
    int togglePin(@Param("id") Long id, @Param("threadId") Long threadId, @Param("userId") int userId); // Changed boolean to int
}