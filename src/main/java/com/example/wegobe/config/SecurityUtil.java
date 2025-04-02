package com.example.wegobe.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * 현재 로그인한 사용자의 kakaoId 가져오기
     */
    public static Long getCurrentKakaoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return Long.parseLong(authentication.getName());
    }
}