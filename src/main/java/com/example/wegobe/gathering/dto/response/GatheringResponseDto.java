package com.example.wegobe.gathering.dto.response;

import com.example.wegobe.auth.dto.response.UserInfoResponseDto;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Category;
import com.example.wegobe.gathering.domain.enums.Gender;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.HashTag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GatheringResponseDto {

    private Long id;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDate startAt;
    private LocalDate endAt;
    private LocalDateTime closedAt;
    private int maxParticipants;
    private String location;
    private Double latitude;
    private Double longitude;

    private Gender preferredGender;
    private AgeGroup preferredAge;
    private Category category;

    private List<String> hashtags;

    private UserInfoResponseDto creator;

    public static GatheringResponseDto fromEntity(Gathering gathering) {
        return GatheringResponseDto.builder()
                .id(gathering.getId())
                .title(gathering.getTitle())
                .content(gathering.getContent())
                .startAt(gathering.getStartAt())
                .endAt(gathering.getEndAt())
                .closedAt(gathering.getClosedAt())
                .maxParticipants(gathering.getMaxParticipants())
                .location(gathering.getLocation())
                .latitude(gathering.getLatitude())
                .longitude(gathering.getLongitude())
                .preferredGender(gathering.getPreferredGender())
                .preferredAge(gathering.getPreferredAge())
                .category(gathering.getCategory())
                .thumbnailUrl(gathering.getThumbnailUrl())
                .hashtags(gathering.getHashtags().stream()
                        .map(HashTag::getTag)
                        .collect(Collectors.toList()))
                .creator(UserInfoResponseDto.builder() // 주최자 정보 변환
                        .kakaoId(gathering.getCreator().getKakaoId())
                        .nickname(gathering.getCreator().getNickname())
                        .email(gathering.getCreator().getEmail())
                        .build())
                .build();
    }

}