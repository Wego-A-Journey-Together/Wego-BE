package com.example.wegobe.gathering.controller;

import com.example.wegobe.gathering.dto.request.GatheringRequestDto;
import com.example.wegobe.gathering.dto.response.GatheringListResponseDto;
import com.example.wegobe.gathering.dto.response.GatheringResponseDto;
import com.example.wegobe.gathering.service.GatheringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gathering", description = "동행 관련 API")
@RestController
@RequestMapping("/api/gatherings")
@RequiredArgsConstructor
public class GatheringController {
    private final GatheringService gatheringService;

    @Operation(summary = "동행 생성", description = "새로운 동행을 등록합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<Long> createGathering(@RequestBody GatheringRequestDto request) {
        Long id = gatheringService.createGathering(request);
        return ResponseEntity.ok(id);
    }
    @Operation(summary = "동행 단일 조회", description = "동행 ID로 해당 동행 정보를 조회합니다.")
    @GetMapping("/{gatheringId}")
    public ResponseEntity<GatheringResponseDto> getGathering(@PathVariable("gatheringId") Long id) {
        GatheringResponseDto response = gatheringService.getGatheringById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "동행 목록 조회", description = "페이지네이션을 기반으로 모든 동행 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<Page<GatheringListResponseDto>> getGatheringList(
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(gatheringService.findAll(pageable));
    }

    @Operation(summary = "동행 수정", description = "동행 ID로 해당 동행의 정보를 수정합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{gatheringId}")
    public ResponseEntity<GatheringResponseDto> updateGathering(
            @PathVariable("gatheringId") Long id,
            @Valid @RequestBody  GatheringRequestDto updateDto) {

        // 동행 수정 후 수정된 동행 정보 반환
        GatheringResponseDto responseDto = gatheringService.updateGathering(id, updateDto);

        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "동행 삭제", description = "동행 ID로 해당 동행을 삭제합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{gatheringId}")
    public ResponseEntity<Void> deleteGathering(@PathVariable("gatheringId") Long id) {
        gatheringService.deleteGathering(id);
        return ResponseEntity.noContent().build();
    }
}
