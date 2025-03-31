package com.example.wegobe.chat.repository;

import com.example.wegobe.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /**
     * 두 유저 간의 채팅방이 존재하는지 조회
     * user1과 user2의 조합으로 이미 생성된 방이 있는지 확인
     * 순서와 관계없이 일치하면 반환
     */
    @Query("SELECT r FROM ChatRoom r WHERE " +
            "(r.user1.kakaoId = :id1 AND r.user2.kakaoId = :id2) OR " +
            "(r.user1.kakaoId = :id2 AND r.user2.kakaoId = :id1)")
    Optional<ChatRoom> findByUsers(@Param("id1") Long id1, @Param("id2") Long id2);

    /**
     * 특정 유저가 포함된 모든 채팅방 조회
     * user1 또는 user2가 있는 채팅방 리스트 반환
     */
    @Query("SELECT r FROM ChatRoom r " +
            "WHERE r.user1.kakaoId = :kakaoId OR r.user2.kakaoId = :kakaoId")
    List<ChatRoom> findAllByUser(@Param("kakaoId") Long kakaoId);

}

