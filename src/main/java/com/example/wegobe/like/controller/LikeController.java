package com.example.wegobe.like.controller;

import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.dto.response.GatheringResponseDto;
import com.example.wegobe.like.dto.LikeResponseDto;
import com.example.wegobe.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gatherings/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    /**
     * 찜 등록 삭제
     */
    @PostMapping("/{gatheringId}")
    public ResponseEntity<LikeResponseDto> aboutLike(@PathVariable Long gatheringId) {
        LikeResponseDto result = likeService.aboutLike(gatheringId);
        return ResponseEntity.ok(result);
    }

    /**
     * 찜 여부 확인
     */
    @GetMapping("/{gatheringId}")
    public ResponseEntity<Boolean> checkLike(@PathVariable Long gatheringId) {
        return ResponseEntity.ok(likeService.isLiked(gatheringId));
    }

    /**
     * 특정 유저의 찜 목록 조회
     */
    @GetMapping("/my")
    public ResponseEntity<List<GatheringResponseDto>> getLikedGatherings() {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        List<GatheringResponseDto> likedGatherings = likeService.getLikedGatherings(kakaoId);
        return ResponseEntity.ok(likedGatherings);
    }
}
