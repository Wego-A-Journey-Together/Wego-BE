package com.example.wegobe.chat.entity;

import com.example.wegobe.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 메시지 저장
 */
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User sender;

    private String message;
    private LocalDateTime sentAt = LocalDateTime.now();

    @Column(name = "is_read")
    private boolean read;

}

