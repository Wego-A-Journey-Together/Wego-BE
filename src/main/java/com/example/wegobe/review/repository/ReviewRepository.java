package com.example.wegobe.review.repository;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByWriterAndGathering(User writer, Gathering gathering);

    Page<Review> findAllByGatheringOrderByCreatedDateDesc(Gathering gathering, Pageable pageable);

    Page<Review> findAllByWriterOrderByCreatedDateDesc(User writer, Pageable pageable);

    Page<Review> findAllByGathering_CreatorOrderByCreatedDateDesc(User creator, Pageable pageable);

    int countByGathering(Gathering gathering);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.gathering.creator.kakaoId = :kakaoId")
    Double findAverageRatingByKakaoId(@Param("kakaoId") Long kakaoId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.gathering.creator.kakaoId = :kakaoId")
    Long countByKakaoId(@Param("kakaoId") Long kakaoId);
}
