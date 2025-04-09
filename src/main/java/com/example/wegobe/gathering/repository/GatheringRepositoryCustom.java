package com.example.wegobe.gathering.repository;

import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.dto.request.GatheringFilterRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GatheringRepositoryCustom {
    Page<Gathering> findByFilters(GatheringFilterRequestDto filter, Pageable pageable);

}
