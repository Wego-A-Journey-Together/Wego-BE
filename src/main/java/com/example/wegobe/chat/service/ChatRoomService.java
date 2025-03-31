package com.example.wegobe.chat.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.chat.dto.ChatRoomSummaryDto;
import com.example.wegobe.chat.entity.ChatMessage;
import com.example.wegobe.chat.entity.ChatRoom;
import com.example.wegobe.chat.repository.ChatMessageRepository;
import com.example.wegobe.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository messageRepo;
    private final UserRepository userRepo;

    public Long getOrCreateRoom(Long myKakaoId, Long opponentKakaoId) {
        return chatRoomRepository.findByUsers(myKakaoId, opponentKakaoId)
                .map(ChatRoom::getId)
                .orElseGet(() -> {
                    User me = userRepo.findByKakaoId(myKakaoId).orElseThrow();
                    User you = userRepo.findByKakaoId(opponentKakaoId).orElseThrow();
                    return chatRoomRepository.save(new ChatRoom(me, you)).getId();
                });
    }

   // 채팅방 목록 조회
    public List<ChatRoomSummaryDto> getRoomSummaries(Long myKakaoId) {
        return chatRoomRepository.findAllByUser(myKakaoId).stream().map(room -> {
            User opponent = room.getUser1().getKakaoId().equals(myKakaoId) ? room.getUser2() : room.getUser1();
            int unread = messageRepo.countUnread(room.getId(), myKakaoId);
            String lastMessage = messageRepo.findByChatRoomIdOrderBySentAtDesc(room.getId()).stream()
                    .reduce((first, second) -> second).map(ChatMessage::getMessage).orElse("");
            return ChatRoomSummaryDto.builder()
                    .roomId(room.getId())
                    .opponentNickname(opponent.getNickname())
                    .lastMessage(lastMessage)
                    .unreadCount(unread)
                    .build();
        }).toList();
    }

    public ChatRoom findById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("해당 채팅방을 찾을 수 없습니다."));
    }
}

