package com.example.wegobe.review.dto;

import com.example.wegobe.profile.WriterProfileDto;
import com.example.wegobe.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ReviewResponseDto {
    private WriterProfileDto writer;
    private String createdAt;
    private Integer rating;
    private String content;

    public static ReviewResponseDto fromEntity(Review review) {
        return ReviewResponseDto.builder()
                .writer(WriterProfileDto.fromEntity(review.getWriter()))
                .createdAt(review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .rating(review.getRating())
                .content(review.getContent())
                .build();
    }
}
