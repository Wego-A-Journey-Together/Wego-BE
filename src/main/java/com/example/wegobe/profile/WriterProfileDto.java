package com.example.wegobe.profile;

import com.example.wegobe.auth.entity.User;
import lombok.Builder;
import lombok.Getter;
/**
 * 댓글-리뷰 유저 조회 dto
 */
@Getter
@Builder
public class WriterProfileDto {
    private String nickname;
    private String thumbnailUrl;

    public static WriterProfileDto fromEntity(User user) {
        return WriterProfileDto.builder()
                .nickname(user.getNickname())
                .thumbnailUrl(user.getThumbnailUrl())
                .build();
    }
}