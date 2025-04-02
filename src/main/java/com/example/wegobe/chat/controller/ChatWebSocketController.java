package com.example.wegobe.chat.controller;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.jwt.JwtUtil;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.auth.service.UserDetailsServiceImpl;
import com.example.wegobe.chat.dto.ChatMessageDto;
import com.example.wegobe.chat.entity.ChatMessage;
import com.example.wegobe.chat.entity.ChatRoom;
import com.example.wegobe.chat.repository.ChatMessageRepository;
import com.example.wegobe.chat.repository.ChatRoomRepository;
import com.example.wegobe.chat.service.ChatRoomService;
import com.example.wegobe.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * websocket이 메시지 처리하는 controller
 */
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {


    private final ChatRoomService chatRoomService;
    private final UserDetailsServiceImpl userService;

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * WebSocket으로 수신한 채팅 메시지를 저장하고 상대방에게 실시간 전송
     * 클라이언트에서 헤더에 Authorization 넣어서 Stomp로
     */
    @Transactional
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto dto, MessageHeaders headers) {
        String token = null;

        // 헤더에서 토큰 추출
        if (headers.containsKey("simpUser")) {
            System.out.println("simpUser 있음: " + headers.get("simpUser"));
        }

        Map<String, Object> nativeHeaders = (Map<String, Object>) headers.get("nativeHeaders");
        if (nativeHeaders != null && nativeHeaders.containsKey("Authorization")) {
            List<String> authHeader = (List<String>) nativeHeaders.get("Authorization");
            token = authHeader.get(0).replace("Bearer ", "");
        }

        if (token == null) {
            System.out.println("토큰 없음!");
            return;
        }

        // 토큰에서 kakaoId 추출
        String kakaoId = JwtUtil.getUsernameFromToken(token);  // username = kakaoId로 저장한 경우
        System.out.println("토큰에서 추출한 kakaoId: " + kakaoId);


        //  사용자, 채팅방 조회
        User sender = userService.findByKakaoId(Long.valueOf(kakaoId));
        ChatRoom room = chatRoomService.findById(dto.getRoomId());

        // 메시지 저장
        ChatMessage savedMessage = chatService.save(dto, sender, room);

        // 실시간 메시지 전송
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + dto.getRoomId(),
                ChatMessageDto.fromEntity(savedMessage)
        );
    }
}