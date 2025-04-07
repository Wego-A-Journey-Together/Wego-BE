package com.example.wegobe.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewRequestDto {
    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    @NotBlank
    private String content;
}