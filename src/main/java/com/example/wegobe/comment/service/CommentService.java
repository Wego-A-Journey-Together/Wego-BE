package com.example.wegobe.comment.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.service.UserService;
import com.example.wegobe.comment.domain.Comment;
import com.example.wegobe.comment.dto.CommentRequestDto;
import com.example.wegobe.comment.dto.CommentResponseDto;
import com.example.wegobe.comment.repository.CommentRepository;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final GatheringRepository gatheringRepository;
    private final UserService userService;
    /**
     * 댓글 등록
     * 부모 댓글이 없을 경우는 새 댓글
     * 부모 댓글이 존재할 경우 대댓글로
     * 댓글 본문 반환
     */
    @Transactional
    public CommentResponseDto addComment(Long gatheringId, CommentRequestDto request) {
        User user = userService.getCurrentUser();
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new RuntimeException("동행을 찾을 수 없습니다."));


        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .writer(user)
                .gathering(gathering)
                .parent(parent)
                .build();

        return CommentResponseDto.fromEntity(commentRepository.save(comment));
    }

    /**
     * 대댓글만 조회
     */
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getReplies(Long gatheringId, Long parentId, Pageable pageable){
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));

        if (!parent.getGathering().getId().equals(gatheringId)) {
            throw new RuntimeException("잘못된 요청입니다. 이 댓글은 해당 동행에 속하지 않습니다.");
        }

        return commentRepository.findByParent(parent, pageable)
                .map(CommentResponseDto::fromEntity);
    }

    /**
     * 대댓글 포함 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getComments(Long gatheringId, Pageable pageable) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new RuntimeException("동행을 찾을 수 없습니다."));

        return commentRepository.findByGatheringAndParentIsNullOrderByCreatedDateAsc(gathering, pageable)
                .map(CommentResponseDto::fromEntity);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentId, String content) {
        User user = userService.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getWriter().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 댓글만 수정할 수 있습니다.");
        }
        comment.updateContent(content);
        return CommentResponseDto.fromEntity(comment);
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId) {
        User user = userService.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getWriter().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    /**
     * 특정 유저가 남긴 댓글 조회
     */
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getMyComments(Pageable pageable) {
        User user = userService.getCurrentUser();

        return commentRepository.findByWriter(user, pageable)
                .map(CommentResponseDto::fromEntity);
    }
}
