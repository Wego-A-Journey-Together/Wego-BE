package com.example.wegobe.gathering.dto.response;

import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Gender;
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
    private String location;
    private Gender preferredGender;
    private AgeGroup preferredAge;

    public static GatheringSimpleResponseDto fromEntity(Gathering gathering) {
        return GatheringSimpleResponseDto.builder()
                .gatheringId(gathering.getId())
                .title(gathering.getTitle())
                .thumbnailUrl(gathering.getThumbnailUrl())
                .startAt(gathering.getStartAt())
                .endAt(gathering.getEndAt())
                .location(gathering.getLocation())
                .preferredGender(gathering.getPreferredGender())
                .preferredAge(gathering.getPreferredAge())
                .build();
    }
}
