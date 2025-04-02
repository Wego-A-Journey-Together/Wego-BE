package com.example.wegobe.like.repository;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndGathering(User user, Gathering gathering);
    boolean existsByUserAndGathering(User user, Gathering gathering);
    void deleteByUserAndGathering(User user, Gathering gathering);
    List<Like> findByUser(User user);

}
