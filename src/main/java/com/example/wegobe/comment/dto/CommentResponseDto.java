package com.example.wegobe.comment.dto;

import com.example.wegobe.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String createdAt;
    private String nickname;
    private Long parentId;
    private List<CommentResponseDto> replies;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .nickname(comment.getWriter().getNickname())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .replies(comment.getChildren().stream()
                        .map(CommentResponseDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
