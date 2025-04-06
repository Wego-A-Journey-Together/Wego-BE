package com.example.wegobe.comment.dto;

import com.example.wegobe.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {
    private String content;
    private Long parentId;

    public static CommentRequestDto fromEntity(Comment comment) {
        return CommentRequestDto.builder()
                .content(comment.getContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .build();
    }

}
