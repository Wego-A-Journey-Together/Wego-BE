package com.example.wegobe.profile;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Gender;
import lombok.Builder;
import lombok.Getter;

/**
 * 유저프로필 공통 조회 dto
 */
@Getter
@Builder
public class UserProfileDto {
    private String nickname;
    private String thumbnailUrl;
    private String statusMessage;
    private Gender gender;
    private AgeGroup ageGroup;

    public static UserProfileDto fromEntity(User user) {
        return UserProfileDto.builder()
                .nickname(user.getNickname())
                .thumbnailUrl(user.getThumbnailUrl())
                .statusMessage(user.getStatusMessage())
                .gender(user.getGender())
                .ageGroup(user.getAgeGroup())
                .build();
    }
}
