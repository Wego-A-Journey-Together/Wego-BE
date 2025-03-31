package com.example.wegobe.chat.controller;

import com.example.wegobe.auth.jwt.JwtUtil;
import com.example.wegobe.chat.service.ChatRoomService;
import com.example.wegobe.chat.service.ChatService;
import com.example.wegobe.config.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 상대방 카카오 ID와의 채팅방을 새로 만들거나, 기존 방 ID를 반환
     */
    @PostMapping("/rooms")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, Long> request) {
        Long roomId = chatRoomService.getOrCreateRoom(getMyId(), request.get("opponentKakaoId"));
        return ResponseEntity.ok(Map.of("roomId", roomId));
    }

    /**
     * 현재 사용자의 채팅방 목록 + 안읽은 메시지 개수 요약 반환
     */
    @GetMapping("/rooms")
    public ResponseEntity<?> getRooms() {
        return ResponseEntity.ok(chatRoomService.getRoomSummaries(getMyId()));
    }

    /**
     * 특정 채팅방의 메시지 전체 조회
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    /**
     * 현재 사용자가 채팅방을 열었을 때, 안읽은 메시지를 모두 읽음 처리
     */
    @PatchMapping("/rooms/{roomId}/read")
    public ResponseEntity<?> markMessagesAsRead(
            @PathVariable Long roomId,
            @RequestHeader("Authorization") String tokenHeader
    ) {
        String token = tokenHeader.replace("Bearer ", "");
        String kakaoId = JwtUtil.getUsernameFromToken(token);

        chatService.markAsRead(roomId, Long.valueOf(kakaoId));
        return ResponseEntity.ok("읽음 처리 완료");
    }

    // 현재 로그인된 사용자의 카카오 ID 반환
    private Long getMyId() {
        return SecurityUtil.getCurrentKakaoId();
    }
}

