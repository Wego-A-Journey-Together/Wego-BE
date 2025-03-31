package com.example.wegobe.chat.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.chat.dto.ChatMessageDto;
import com.example.wegobe.chat.entity.ChatMessage;
import com.example.wegobe.chat.entity.ChatRoom;
import com.example.wegobe.chat.repository.ChatMessageRepository;
import com.example.wegobe.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepo;

    // 메시지 저장
    public ChatMessage save(ChatMessageDto dto, User sender, ChatRoom room) {
        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .sender(sender)
                .message(dto.getMessage())
                .sentAt(LocalDateTime.now())
                .read(false)
                .build();

        return messageRepo.save(message);
    }

    // 과거 메시지 조회
    public List<ChatMessageDto> getMessages(Long roomId) {
        return messageRepo.findByChatRoomIdOrderBySentAtDesc(roomId).stream()
                .map(ChatMessageDto::fromEntity)
                .toList();
    }

    // 읽음 처리
    public void markAsRead(Long roomId, Long kakaoId) {
        messageRepo.markMessagesAsRead(roomId, kakaoId);
    }
}

