package com.example.wegobe.auth.dto.response;

import lombok.Data;

// 카카오 사용자 정보 응답 DTO
@Data
public class KakaoUserInfoResponse {
    private Long id; // 카카오 유저 고유 번호
    private KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        private Profile profile;
        private String email; // 사용자의 이메일

        @Data
        public static class Profile {
            private String nickname; // 사용자의 닉네임
        }
    }
}
