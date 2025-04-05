package com.example.wegobe.comment.repository;

import com.example.wegobe.comment.domain.Comment;
import com.example.wegobe.gathering.domain.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 부모 댓글만 달려있는 순서대로(오름차순)
    Page<Comment> findByGatheringAndParentIsNullOrderByCreatedDateAsc(Gathering gathering, Pageable pageable);

    // 대댓글만 조회할 경우
    Page<Comment> findByParent(Comment parent, Pageable pageable);
}