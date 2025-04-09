package com.example.wegobe.comment.controller;

import com.example.wegobe.comment.dto.CommentRequestDto;
import com.example.wegobe.comment.dto.CommentResponseDto;
import com.example.wegobe.comment.service.CommentService;
import com.example.wegobe.config.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatherings")
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @Operation(summary = "댓글 등록", description = "새로운 댓글을 등록합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{gatheringId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long gatheringId,
                                                         @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(gatheringId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 대댓글만 조회
    @Operation(summary = "대댓글 조회", description = "특정 댓글의 대댓글만 조회합니다.")
    @GetMapping("/{gatheringId}/comments/{parentId}/replies")
    public ResponseEntity<Page<CommentResponseDto>> getReplies(@PathVariable Long gatheringId, @PathVariable Long parentId,
                                                               @PageableDefault(page=0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(commentService.getReplies(gatheringId, parentId, pageable));
    }

    // 모든 댓글 조회
    @Operation(summary = "대댓글 포함 댓글 조회", description = "20개 기준 오름차순으로 댓글 목록을 조회합니다.")
    @GetMapping("/{gatheringId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsWithReplies(@PathVariable Long gatheringId,
                                                                           @PageableDefault(page=0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(commentService.getComments(gatheringId, pageable));
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody String content) {
        CommentResponseDto response = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(response);
    }
    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}