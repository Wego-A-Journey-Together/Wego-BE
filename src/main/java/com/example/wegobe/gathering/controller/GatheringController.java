package com.example.wegobe.gathering.controller;

import com.example.wegobe.gathering.dto.request.GatheringRequestDto;
import com.example.wegobe.gathering.service.GatheringService;
import lombok.RequiredArgsConstructor;
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

}
