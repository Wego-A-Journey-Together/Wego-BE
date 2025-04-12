package com.example.wegobe.gathering.dto.response;

import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Gender;
import com.example.wegobe.profile.WriterProfileDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 마이페이지에서 사용되는 소감 남기지 않은 동행 정보 dto
 */
@Getter
@Builder
public class GatheringSimpleResponseDto {
    private Long gatheringId;
    private String title;
    private String thumbnailUrl;
    private LocalDate startAt;
    private LocalDate endAt;
    private Integer maxParticipants;
    private int currentParticipants;
    private Gender preferredGender;
    private AgeGroup preferredAge;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WriterProfileDto host;

    public static GatheringSimpleResponseDto fromEntity(Gathering gathering, int currentParticipants,WriterProfileDto host ) {
        return GatheringSimpleResponseDto.builder()
                .gatheringId(gathering.getId())
                .title(gathering.getTitle())
                .thumbnailUrl(gathering.getThumbnailUrl())
                .startAt(gathering.getStartAt())
                .endAt(gathering.getEndAt())
                .maxParticipants(gathering.getMaxParticipants())
                .currentParticipants(currentParticipants)
                .preferredGender(gathering.getPreferredGender())
                .preferredAge(gathering.getPreferredAge())
                .host(host)
                .build();
    }

    public static GatheringSimpleResponseDto fromEntity(Gathering gathering, int currentParticipants) {
        return fromEntity(gathering, currentParticipants, null);
    }
}
