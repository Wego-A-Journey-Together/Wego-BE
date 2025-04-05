package com.example.wegobe.comment.repository;

import com.example.wegobe.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
}
