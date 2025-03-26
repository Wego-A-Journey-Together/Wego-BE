package com.example.wegobe.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 사용자 데이터 확인
@Getter
@AllArgsConstructor
@Builder
public class UserInfoResponseDto {
    private long kakaoId;
    private String nickname;
    private String email; // null 가능
}

