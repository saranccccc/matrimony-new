package com.matrimony.chat.repository;

import com.matrimony.chat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUser1IdAndUser2Id(String user1, String user2);

    List<Conversation> findByUser1IdOrUser2Id(String user1, String user2);
}
