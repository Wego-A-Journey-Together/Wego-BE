package com.example.wegobe.gathering.controller;

import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.service.GatheringMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/gatherings")
@RequiredArgsConstructor
public class GatheringMemberController {
    private final GatheringMemberService gatheringMemberService;

    /**
     * 동행 참여 신청
     */
    @PostMapping("/apply/{gatheringId}")
    public ResponseEntity<String> apply(@PathVariable("gatheringId") Long gatheringId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        try {
            gatheringMemberService.applyGathering(gatheringId, kakaoId);
            return ResponseEntity.status(HttpStatus.CREATED).body("동행 신청이 완료되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * 동행 신청 취소
     */
    @DeleteMapping("/cancel/{gatheringId}/")
    public ResponseEntity<String> cancelApply(@PathVariable Long gatheringId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        gatheringMemberService.cancelApplying(gatheringId, kakaoId);
        return ResponseEntity.ok("동행 신청이 취소되었습니다.");
    }
}