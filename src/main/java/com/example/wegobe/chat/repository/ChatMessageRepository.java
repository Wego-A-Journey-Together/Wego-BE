package com.example.wegobe.chat.repository;

import com.example.wegobe.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 특정 채팅방의 모든 메시지를 발송 시간 기준 내림차순 으로 조회 -> 최신순
     *
     * @param roomId 채팅방 ID
     * @return 정렬된 메시지 목록
     */
    List<ChatMessage> findByChatRoomIdOrderBySentAtDesc(Long roomId);

    /**
     * 안 읽은 메시지 수를 카운트함
     * 내가 보낸 메시지는 제외하고, 상대방이 보낸 읽지 않은 메시지만 카운트
     *
     * @param roomId 채팅방 ID
     * @param myKakaoId 현재 사용자(Kakao ID)
     * @return 읽지 않은 메시지 수
     */
    @Query("""
    SELECT COUNT(m) FROM ChatMessage m
    WHERE m.chatRoom.id = :roomId
      AND m.sender.kakaoId <> :myKakaoId
      AND m.read = false
""")
    int countUnread(@Param("roomId") Long roomId, @Param("myKakaoId") Long myKakaoId);

    /**
     * 특정 채팅방에서 내가 아닌 사람이 보낸 읽지 않은 메시지를 모두 읽음 처리
     *
     * @param roomId 채팅방 ID
     * @param myId 현재 사용자(Kakao ID)
     */
    @Modifying
    @Query("UPDATE ChatMessage m SET m.read = true WHERE m.chatRoom.id = :roomId AND m.sender.kakaoId <> :myId AND m.read = false")
    @Transactional
    void markMessagesAsRead(@Param("roomId") Long roomId, @Param("myId") Long myId);
}

