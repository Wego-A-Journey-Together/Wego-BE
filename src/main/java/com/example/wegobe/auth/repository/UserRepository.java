package com.example.wegobe.auth.repository;


import com.example.wegobe.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // kakaoId로 사용자 정보 가져오기
    Optional<User> findByKakaoId(Long kakaoId);
}
