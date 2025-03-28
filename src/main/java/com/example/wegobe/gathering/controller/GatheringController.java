package com.example.wegobe.gathering.controller;

import com.example.wegobe.gathering.dto.request.GatheringRequestDto;
import com.example.wegobe.gathering.dto.response.GatheringListResponseDto;
import com.example.wegobe.gathering.dto.response.GatheringResponseDto;
import com.example.wegobe.gathering.service.GatheringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/gatherings")
@RequiredArgsConstructor
public class GatheringController {
    private final GatheringService gatheringService;

    @PostMapping
    public ResponseEntity<Long> createGathering(@RequestBody GatheringRequestDto request) {
        Long id = gatheringService.createGathering(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{gatheringId}")
    public ResponseEntity<GatheringResponseDto> getGathering(@PathVariable("gatheringId") Long id) {
        GatheringResponseDto response = gatheringService.getGatheringById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/list")
    public ResponseEntity<Page<GatheringListResponseDto>> getGatheringList(
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(gatheringService.findAll(pageable));
    }

    @PatchMapping("/{gatheringId}")
    public ResponseEntity<GatheringResponseDto> updateGathering(
            @PathVariable("gatheringId") Long id,
            @Valid @RequestBody  GatheringRequestDto updateDto) {

        // 동행 수정 후 수정된 동행 정보 반환
        GatheringResponseDto responseDto = gatheringService.updateGathering(id, updateDto);

        return ResponseEntity.ok(responseDto);
    }
    @DeleteMapping("/{gatheringId}")
    public ResponseEntity<Void> deleteGathering(@PathVariable("gatheringId") Long id) {
        gatheringService.deleteGathering(id);
        return ResponseEntity.noContent().build();
    }
}
