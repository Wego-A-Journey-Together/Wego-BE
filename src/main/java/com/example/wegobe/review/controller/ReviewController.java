package com.example.wegobe.review.controller;

import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.review.dto.ReviewRequestDto;
import com.example.wegobe.review.dto.ReviewResponseDto;
import com.example.wegobe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review", description = "소감 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @Operation(summary = "소감 등록", description = "내가 참여한 동행에만 리뷰를 등록할 수 있습니다.(이미 등록한 경우에도 재등록X)")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{gatheringId}")
    public ResponseEntity<Long> writeReview(@PathVariable Long gatheringId,
                                            @RequestBody ReviewRequestDto requestDto) {
        Long reviewId = reviewService.writeReview(gatheringId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

    // 동행별 리뷰 조회
    @Operation(summary = "특정 동행에 대한 소감 조회", description = "특정 동행에 달린 소감을 조회할 수 있습니다.")
    @GetMapping("/gathering/{gatheringId}")
    public Page<ReviewResponseDto> getReviewsByGathering(@PathVariable Long gatheringId, @PageableDefault(page=0, size = 10) Pageable pageable) {
        return reviewService.getReviewsByGathering(gatheringId, pageable);
    }

    // 내가 참여했지만, 아직 소감 등록 안한 동행 정보 조회
    @Operation(summary = "작성 가능한 소감 조회", description = "동행에 참여했지만, 아직 남기지 않은 소감 목록을 조회할 수 있습니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/unwritten")
    public ResponseEntity<List<GatheringSimpleResponseDto>> getUnwrittenReviewGatherings() {
        return ResponseEntity.ok(reviewService.getUnwrittenReviewGatherings());
    }

    // 특정 유저에게 남겨진 소감 목록 조회
    @Operation(summary = "특정 유저가 받은 소감 조회", description = "해당 유저가 주최한 동행에 달린 소감을 조회할 수 있습니다.")
    @GetMapping("/user/{kakaoId}/received")
    public Page<ReviewResponseDto> getReviewsForUserGatherings(@PathVariable(name = "kakaoId") Long kakaoId,
                                                               @PageableDefault(page=0, size = 10) Pageable pageable) {
        return reviewService.getReviewsForUserGatherings(kakaoId, pageable);
    }

}
