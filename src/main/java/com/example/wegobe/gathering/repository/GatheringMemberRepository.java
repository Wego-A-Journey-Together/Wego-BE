package com.example.wegobe.gathering.repository;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.GatheringMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GatheringMemberRepository extends JpaRepository<GatheringMember, Long> {
    Optional<GatheringMember> findByUserAndGathering(User user, Gathering gathering);

}