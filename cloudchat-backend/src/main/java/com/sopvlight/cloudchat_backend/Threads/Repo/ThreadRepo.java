package com.sopvlight.cloudchat_backend.Threads.Repo;

import com.sopvlight.cloudchat_backend.Threads.Entity.Thread;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

// import org.hibernate.annotations.Changelog.ModifiedEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepo extends JpaRepository<Thread, Long> {

    @Query("SELECT t FROM Thread t WHERE t.id = :id AND :userId MEMBER OF t.participantIds")
    Optional<Thread> findByIdAndParticipantId(@Param("id") Long id, @Param("userId") int userId);

    @Query("SELECT t FROM Thread t WHERE :userId MEMBER OF t.participantIds AND t.threadType = :type")
    List<Thread> findAllForMy(@Param("userId") int userId, @Param("type") String type);

    @Transactional
    @Modifying
    @Query("DELETE FROM Thread t WHERE t.creatorId = :creatorId AND t.id = :threadId")
    int deleteByCreatorIdAndThreadId(@Param("creatorId") int creatorId, @Param("threadId") Long threadId);

}
