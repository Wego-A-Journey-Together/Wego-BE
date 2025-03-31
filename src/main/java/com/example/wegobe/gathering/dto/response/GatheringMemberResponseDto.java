package com.example.wegobe.gathering.dto.response;

import com.example.wegobe.gathering.domain.GatheringMember;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * 동행 참여 신청되어있는 유저 목록 조회에서 사용
 * 유저 프로필 생성되었을 경우 필드 추가 혹은 프로필 response로 변경될 수 있음
 */
@Getter
@Builder
public class GatheringMemberResponseDto {
    private Long userId;
    private String nickname;
    private GatheringStatus status;

    public static GatheringMemberResponseDto fromEntity(GatheringMember member) {
        return GatheringMemberResponseDto.builder()
                .userId(member.getUser().getId())
                .nickname(member.getUser().getNickname())
                .status(member.getStatus())
                .build();
    }
}