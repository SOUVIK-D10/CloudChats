package com.sopvlight.cloudchat_backend.Threads.Repo;

import com.sopvlight.cloudchat_backend.Threads.Entity.Thread;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepo extends JpaRepository<Thread, Long> {

    @Query("SELECT t FROM Thread t WHERE t.id = :id AND :userId MEMBER OF t.participantIds")
    Optional<Thread> findByIdAndParticipantId(@Param("id") Long id, @Param("userId") int userId);

    @Query("SELECT t FROM Thread t WHERE :userId MEMBER OF t.participantIds AND t.threadType = :type")
    List<Thread> findAllForMy(@Param("userId") int userId, @Param("type") String type);

    // Optional<Thread> findByIdAndParticipantId(Long id, int userId);

}
