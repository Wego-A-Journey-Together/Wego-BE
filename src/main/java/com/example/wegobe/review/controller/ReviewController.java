package com.example.wegobe.review.controller;

import com.example.wegobe.review.dto.ReviewRequestDto;
import com.example.wegobe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{gatheringId}")
    public ResponseEntity<Long> writeReview(@PathVariable Long gatheringId,
                                            @RequestBody ReviewRequestDto requestDto) {
        Long reviewId = reviewService.writeReview(gatheringId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }
}
