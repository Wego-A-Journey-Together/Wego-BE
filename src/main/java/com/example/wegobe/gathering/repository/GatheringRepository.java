package com.example.wegobe.gathering.repository;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GatheringRepository extends JpaRepository<Gathering, Long>, GatheringRepositoryCustom {
    @Query("SELECT DISTINCT g FROM Gathering g LEFT JOIN g.hashtags h " +
            "WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(h.tag) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Gathering> searchByTitleOrHashtag(@Param("keyword") String keyword, Pageable pageable);

    Page<Gathering> findByCreator(User user, Pageable pageable);
}