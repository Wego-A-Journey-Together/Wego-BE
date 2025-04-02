package com.example.wegobe.like.controller;

import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.dto.response.GatheringResponseDto;
import com.example.wegobe.like.dto.LikeResponseDto;
import com.example.wegobe.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Like", description = "동행 찜 API")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/gatherings/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    /**
     * 찜 등록 삭제
     */
    @Operation(summary = "동행 찜 등록 및 삭제", description = "동행이 이미 찜 되어있을 때, 찜 취소 // 아닐 경우 새로 찜 등록")
    @PostMapping("/{gatheringId}")
    public ResponseEntity<LikeResponseDto> aboutLike(@PathVariable Long gatheringId) {
        LikeResponseDto result = likeService.aboutLike(gatheringId);
        return ResponseEntity.ok(result);
    }

    /**
     * 찜 여부 확인
     */
    @Operation(summary = "찜 여부 확인", description = "현재 사용자가 해당 동행을 찜했는지 여부를 반환합니다.")
    @GetMapping("/{gatheringId}")
    public ResponseEntity<Boolean> checkLike(@PathVariable Long gatheringId) {
        return ResponseEntity.ok(likeService.isLiked(gatheringId));
    }

    /**
     * 특정 유저의 찜 목록 조회
     */
    @Operation(summary = "내 찜 목록 조회", description = "현재 사용자의 찜한 동행 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<GatheringResponseDto>> getLikedGatherings() {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        List<GatheringResponseDto> likedGatherings = likeService.getLikedGatherings(kakaoId);
        return ResponseEntity.ok(likedGatherings);
    }
}
