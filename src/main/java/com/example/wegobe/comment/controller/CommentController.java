package com.example.wegobe.comment.controller;

import com.example.wegobe.comment.dto.CommentRequestDto;
import com.example.wegobe.comment.dto.CommentResponseDto;
import com.example.wegobe.comment.service.CommentService;
import com.example.wegobe.config.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatherings")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{gatheringId}/comments")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long gatheringId,
                                                         @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(gatheringId, SecurityUtil.getCurrentKakaoId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}