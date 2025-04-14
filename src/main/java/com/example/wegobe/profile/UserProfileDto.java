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
    private Long kakaoId;
    private String nickname;
    private String thumbnailUrl;
    private String statusMessage;
    private Gender gender;
    private AgeGroup ageGroup;

    private Double averageRating;
    private Long totalReviews;

    public static UserProfileDto fromEntity(User user, Double averageRating, Long totalReviews) {
        return UserProfileDto.builder()
                .kakaoId(user.getKakaoId())
                .nickname(user.getNickname())
                .thumbnailUrl(
                        user.getThumbnailUrl() != null ? user.getThumbnailUrl() :
                                "https://wegotiptaparticleimageuploadersuperultraggorgeousbucket.s3.ap-northeast-2.amazonaws.com/default/yaho"
                )
                .statusMessage(user.getStatusMessage())
                .gender(user.getGender())
                .ageGroup(user.getAgeGroup())
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .build();
    }
    public static UserProfileDto fromEntity(User user) {
        return fromEntity(user, null, null);
    }
}
