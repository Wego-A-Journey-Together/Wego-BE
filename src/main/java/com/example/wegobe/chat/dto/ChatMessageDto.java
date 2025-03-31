package com.example.wegobe.chat.dto;

import com.example.wegobe.chat.entity.ChatMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 메시지 전송용
 */
@Data
public class ChatMessageDto {
    private Long roomId;
    private String message;
    private java.time.LocalDateTime sentAt;


    public static ChatMessageDto fromEntity(ChatMessage m) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setRoomId(m.getChatRoom().getId());
        dto.setMessage(m.getMessage());
        dto.setSentAt(m.getSentAt());
        return dto;
    }
}


