package com.example.wegobe.comment.controller;

import com.example.wegobe.comment.dto.CommentRequestDto;
import com.example.wegobe.comment.dto.CommentResponseDto;
import com.example.wegobe.comment.service.CommentService;
import com.example.wegobe.config.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatherings")
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/{gatheringId}/comments")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long gatheringId,
                                                         @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(gatheringId, SecurityUtil.getCurrentKakaoId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 대댓글만 조회
    @GetMapping("/{gatheringId}/comments/{parentId}/replies")
    public ResponseEntity<Page<CommentResponseDto>> getReplies(@PathVariable Long gatheringId, @PathVariable Long parentId,
                                                               Pageable pageable) {
        return ResponseEntity.ok(commentService.getReplies(gatheringId, parentId, pageable));
    }

    // 모든 댓글 조회
    @GetMapping("/{gatheringId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsWithReplies(@PathVariable Long gatheringId,
                                                                           @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(commentService.getComments(gatheringId, pageable));
    }
}