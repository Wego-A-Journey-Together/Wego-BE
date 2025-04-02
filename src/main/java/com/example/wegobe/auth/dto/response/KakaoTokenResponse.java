package com.example.wegobe.auth.dto.response;

import lombok.Data;

//  카카오 토큰 응답을 받을 DTO
@Data
public class KakaoTokenResponse {
    private String token_type;
    private String access_token;
    private String refresh_token;
    private int expires_in; // access token 만료 시간
    private int refresh_token_expires_in; // refresh token 만료 시간
    private String scope;
}
