package com.example.wegobe.profile.controller;

import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.gathering.service.GatheringMemberService;
import com.example.wegobe.gathering.service.GatheringService;
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

    @GetMapping("/gathering/created")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Page<GatheringSimpleResponseDto>> getMyGatherings(@PageableDefault(size = 20, sort = "createdDate",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(gatheringService.getMyGatherings(pageable));
    }

    @GetMapping("/joined")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<GatheringSimpleResponseDto>> getJoinedGatherings() {
        return ResponseEntity.ok(gatheringMemberService.getMyJoinedGatherings());
    }

}
