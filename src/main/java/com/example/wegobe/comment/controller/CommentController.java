package com.example.wegobe.comment.controller;

import com.example.wegobe.comment.dto.CommentRequestDto;
import com.example.wegobe.comment.dto.CommentResponseDto;
import com.example.wegobe.comment.service.CommentService;
import com.example.wegobe.config.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody String content) {
        CommentResponseDto response = commentService.updateComment(commentId, SecurityUtil.getCurrentKakaoId(), content);
        return ResponseEntity.ok(response);
    }
    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId, SecurityUtil.getCurrentKakaoId());
        return ResponseEntity.noContent().build();
    }

    // 특정 유저가 남긴 댓글 조회
    @GetMapping("/users/me/comments")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Page<CommentResponseDto>> getMyComments(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        return ResponseEntity.ok(commentService.getMyComments(kakaoId, pageable));
    }
}