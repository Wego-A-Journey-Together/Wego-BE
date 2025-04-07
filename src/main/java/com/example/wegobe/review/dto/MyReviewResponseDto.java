package com.example.wegobe.review.dto;

import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class MyReviewResponseDto {
    private String createdAt;
    private Integer rating;
    private String content;
    private GatheringSimpleResponseDto gathering; // 내가 쓴 리뷰의 대상 동행

    public static MyReviewResponseDto fromEntity(Review review) {
        return MyReviewResponseDto.builder()
                .createdAt(review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .rating(review.getRating())
                .content(review.getContent())
                .gathering(GatheringSimpleResponseDto.fromEntity(review.getGathering()))
                .build();
    }
}
