package com.matrimony.chat.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.chat.entity.Conversation;
import com.matrimony.chat.entity.Message;
import com.matrimony.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/conversation/{otherUserId}")
    public Conversation startChat(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String otherUserId) {
        return chatService.getOrCreateConversation(user.getUserId(), otherUserId);
    }

    @PostMapping("/{conversationId}/message")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long conversationId, @RequestParam String content) {
        chatService.sendMessage(user.getUserId(), conversationId, content);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/{conversationId}/messages")
    public Page<Message> getMessages(@PathVariable Long conversationId, Pageable pageable) {
        return chatService.getMessages(conversationId, pageable);
    }
}
