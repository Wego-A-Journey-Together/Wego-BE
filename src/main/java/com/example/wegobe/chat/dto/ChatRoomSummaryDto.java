package com.example.wegobe.chat.dto;

import lombok.Builder;
import lombok.Getter;

/**
 *  채팅방 목록 조회용
 */
@Getter
@Builder
public class ChatRoomSummaryDto {
    private Long roomId;
    private String opponentNickname;
    private String lastMessage;
    private int unreadCount;
    private String sentAt;
}
