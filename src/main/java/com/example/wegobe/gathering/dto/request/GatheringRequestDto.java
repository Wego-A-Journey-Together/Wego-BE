package com.example.wegobe.gathering.dto.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Category;
import com.example.wegobe.gathering.domain.enums.Gender;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GatheringRequestDto {

    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime closedAt;

    private String thumbnailUrl;

    private String address;
    private Double latitude;
    private Double longitude;

    private int maxParticipants;

    private Gender preferredGender;
    private AgeGroup preferredAgeGroup;
    private Category category;

    private List<String> hashtags;
}
