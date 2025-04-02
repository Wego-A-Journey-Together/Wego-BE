package com.example.wegobe.gathering.controller;

import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.dto.response.GatheringMemberResponseDto;
import com.example.wegobe.gathering.service.GatheringMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "GatheringMember", description = "동행 참여 관련 API")
@RestController
@RequestMapping("/api/gatherings")
@RequiredArgsConstructor
public class GatheringMemberController {
    private final GatheringMemberService gatheringMemberService;

    /**
     * 동행 참여 신청
     */
    @Operation(summary = "동행 신청", description = "현재 로그인한 사용자가 특정 동행에 참여를 신청합니다. (인증 필요)")
    @SecurityRequirement(name = "Bearer Authentication")
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
    @Operation(summary = "동행 신청 취소", description = "현재 로그인한 사용자가 신청한 동행을 취소합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/cancel/{gatheringId}/")
    public ResponseEntity<String> cancelApply(@PathVariable Long gatheringId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        gatheringMemberService.cancelApplying(gatheringId, kakaoId);
        return ResponseEntity.ok("동행 신청이 취소되었습니다.");
    }
    /**
     * 동행 신청 수락
     */
    @Operation(summary = "동행 신청 수락", description = "주최자가 신청자의 참여 요청을 수락합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{gatheringId}/accept/{userId}")
    public ResponseEntity<String> acceptApplication(@PathVariable Long gatheringId, @PathVariable Long userId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        gatheringMemberService.acceptApply(gatheringId, userId, kakaoId);
        return ResponseEntity.ok("동행 신청을 수락하였습니다.");
    }

    /**
     * 수락된 동행 취소 (수락 철회)
     */
    @Operation(summary = "수락 철회", description = "주최자가 이미 수락된 참여자의 참여를 취소합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{gatheringId}/block/{userId}")
    public ResponseEntity<String> cancelAcceptedApplication(@PathVariable Long gatheringId, @PathVariable Long userId) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        gatheringMemberService.cancelParticipator(gatheringId, userId, kakaoId);
        return ResponseEntity.ok("동행 참여를 취소하였습니다.");
    }

    /**
     * 해당 동행의 신청된 유저들 목록 조회
     */
    @Operation(summary = "신청자 목록 조회", description = "주최자가 본인의 동행에 신청한 유저 목록을 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/appliers/{gatheringId}")
    public ResponseEntity<List<GatheringMemberResponseDto>> getAppliers(@PathVariable Long gatheringId) {

        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        return ResponseEntity.ok(gatheringMemberService.getAppliersList(gatheringId, kakaoId));
    }

    /**
     * 특정 동행에 참여중인 유저들 목록 조회
     * 동행 상세 페이지에서 사용될 API
     */
    @Operation(
            summary = "참여자 목록 조회",
            description = "특정 동행에 참여가 수락된 유저 목록을 조회합니다.")
    @GetMapping("/participants/{gatheringId}")
    public ResponseEntity<List<GatheringMemberResponseDto>> getParticipants(@PathVariable Long gatheringId) {

        return ResponseEntity.ok(gatheringMemberService.getParticipantsList(gatheringId));
    }
}