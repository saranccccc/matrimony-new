package com.matrimony.chat.service;

import com.matrimony.auth.service.UserValidationService;
import com.matrimony.chat.entity.Conversation;
import com.matrimony.chat.entity.Message;
import com.matrimony.chat.repository.ConversationRepository;
import com.matrimony.chat.repository.MessageRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.interest.dto.InterestStatus;
import com.matrimony.interest.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final InterestRepository interestRepository;
    private final UserValidationService userValidationService;
    @Transactional
    public Conversation getOrCreateConversation(String senderId, String receiverId) {
        userValidationService.validateUser(senderId);
        userValidationService.validateUser(receiverId);
        String user1 = senderId.compareTo(receiverId) < 0 ? senderId : receiverId;
        String user2 = senderId.compareTo(receiverId) < 0 ? receiverId : senderId;
        return conversationRepository.findByUser1IdAndUser2Id(user1, user2).orElseGet(() -> {
            validateInterestAccepted(senderId, receiverId);
            Conversation conversation = Conversation.builder().user1Id(user1).user2Id(user2).isBlocked(false).build();
            return conversationRepository.save(conversation);
        });
    }

    private void validateInterestAccepted(String userA, String userB) {
        boolean accepted = interestRepository.findBySenderUserIdAndReceiverUserId(userA, userB).map(i -> i.getStatus() == InterestStatus.ACCEPTED).orElse(false);
        if (!accepted) {
            throw new CustomException(ErrorCode.CHAT_NOT_ALLOWED);
        }
    }

    @Transactional
    public void sendMessage(String senderId, Long conversationId, String content) {
        Message message = Message.builder().conversationId(conversationId).senderUserId(senderId).content(content).isRead(false).build();
        messageRepository.save(message);
    }

    public Page<Message> getMessages(Long conversationId, Pageable pageable) {
        return messageRepository.findByConversationId(conversationId, pageable);
    }
}
