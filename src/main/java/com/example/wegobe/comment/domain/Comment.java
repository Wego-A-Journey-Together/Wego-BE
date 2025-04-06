package com.example.wegobe.comment.domain;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = false)
    private Gathering gathering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // 대댓글일 경우 부모 댓글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();


    @Builder
    public Comment(String content, Gathering gathering, User writer, Comment parent) {
        this.content = content;
        this.gathering = gathering;
        this.writer = writer;
        this.parent = parent;
    }

    public void updateContent(String content) {
        this.content = content;
    }
    
}
