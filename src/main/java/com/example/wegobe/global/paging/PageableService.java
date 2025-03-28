package com.example.wegobe.global.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 공통 페이징 인터페이스
 * @param <T> Entity
 * @param <R> 응답 DTO
 * JPA의 Pageable 사용하여 페이징 처리
 */
public interface PageableService<T, R>{
    Page<R> findAll(Pageable pageable);
}