package com.example.wegobe.profile.controller;

import com.example.wegobe.comment.dto.CommentResponseDto;
import com.example.wegobe.comment.service.CommentService;
import com.example.wegobe.gathering.dto.response.GatheringListResponseDto;
import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.gathering.service.GatheringMemberService;
import com.example.wegobe.gathering.service.GatheringService;
import com.example.wegobe.like.service.LikeService;
import com.example.wegobe.review.dto.MyReviewResponseDto;
import com.example.wegobe.review.dto.ReviewResponseDto;
import com.example.wegobe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "MyPage", description = "MyPage API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class MypageController {

    private final GatheringService gatheringService;
    private final GatheringMemberService gatheringMemberService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final ReviewService reviewService;

    @Operation(summary = "내가 만든 동행 조회", description = "내가 생성한 동행 목록을 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/gathering/created")
    public ResponseEntity<Page<GatheringSimpleResponseDto>> getMyGatherings(
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(gatheringService.getMyGatherings(pageable));
    }

    @Operation(summary = "내가 참여 중인 동행 조회", description = "내가 참여하고 있는 동행 목록을 조회합니다.(주최자로 인해 신청이 받아들여져있는)")
    @GetMapping("/gathering/joined")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<GatheringSimpleResponseDto>> getJoinedGatherings() {
        return ResponseEntity.ok(gatheringMemberService.getMyJoinedGatherings());
    }

    // 특정 유저가 남긴 댓글 조회
    @Operation(summary = "내가 남긴 댓글 조회", description = "유저가 남긴 댓글을 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getMyComments(
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(commentService.getMyComments(pageable));
    }

    /**
     * 특정 유저의 찜 목록 조회
     */
    @Operation(summary = "내 찜 목록 조회", description = "현재 사용자의 찜한 동행 목록을 조회합니다.")
    @GetMapping("/likes")
    public ResponseEntity<List<GatheringListResponseDto>> getLikedGatherings() {
        List<GatheringListResponseDto> likedGatherings = likeService.getLikedGatherings();
        return ResponseEntity.ok(likedGatherings);
    }

    // 내가 쓴 리뷰 조회
    @Operation(summary = "내가 쓴 소감들 조회", description = "내가 참여한 동행에 대해 작성한 소감을 조회할 수 있습니다.(최신순)")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/reviews")
    public Page<MyReviewResponseDto> getMyReviews(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return reviewService.getMyReviews(pageable);
    }

    // 내가 주최한 동행에 대한 리뷰
    @Operation(summary = "내가 받은 소감 조회", description = "내가 주최한 동행에 달린 소감을 조회할 수 있습니다.(최신순)")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/reviews/received")
    public Page<ReviewResponseDto> getReviewsForMyGatherings(@PageableDefault(size = 20) Pageable pageable) {
        return reviewService.getReviewsForMyGatherings(pageable);
    }
}
