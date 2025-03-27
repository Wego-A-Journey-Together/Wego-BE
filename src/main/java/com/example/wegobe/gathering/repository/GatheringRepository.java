package com.example.wegobe.gathering.repository;

import com.example.wegobe.gathering.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {
}
