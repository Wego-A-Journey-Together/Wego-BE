package com.example.wegobe.gathering.controller;

import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.dto.response.GatheringMemberResponseDto;
import com.example.wegobe.gathering.service.GatheringMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    /**
     * 동행 신청 수락
     */
    @PatchMapping("/{gatheringId}/accept/{userId}")
    public ResponseEntity<String> acceptApplication(@PathVariable Long gatheringId, @PathVariable Long userId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        gatheringMemberService.acceptApply(gatheringId, userId, kakaoId);
        return ResponseEntity.ok("동행 신청을 수락하였습니다.");
    }

    /**
     * 수락된 동행 취소 (수락 철회)
     */
    @PatchMapping("/{gatheringId}/block/{userId}")
    public ResponseEntity<String> cancelAcceptedApplication(@PathVariable Long gatheringId, @PathVariable Long userId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        gatheringMemberService.cancelParticipator(gatheringId, userId, kakaoId);
        return ResponseEntity.ok("동행 참여를 취소하였습니다.");
    }
    /**
     * 해당 동행의 신청된 유저들 목록 조회
     */
    @GetMapping("/appliers/{gatheringId}")
    public ResponseEntity<List<GatheringMemberResponseDto>> getAppliers(@PathVariable Long gatheringId) {

        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        return ResponseEntity.ok(gatheringMemberService.getAppliersList(gatheringId, kakaoId));
    }

    /**
     * 특정 동행에 참여중인 유저들 목록 조회
     */
    @GetMapping("/participants/{gatheringId}")
    public ResponseEntity<List<GatheringMemberResponseDto>> getParticipants(@PathVariable Long gatheringId) {

        return ResponseEntity.ok(gatheringMemberService.getParticipantsList(gatheringId));
    }
}