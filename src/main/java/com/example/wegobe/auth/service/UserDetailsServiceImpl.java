package com.example.wegobe.auth.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // kakaoId - String을 기반으로 조회
    @Override
    public UserDetails loadUserByUsername(String kakaoIdStr) throws UsernameNotFoundException {
        Long kakaoId;
        try {
            kakaoId = Long.parseLong(kakaoIdStr);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("잘못된 kakaoId 형식: " + kakaoIdStr);
        }

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 (kakaoId): " + kakaoId));

        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(kakaoId)) // JWT subject로 kakaoId를 사용
                .password("kakao") // 사용되지 않음
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}