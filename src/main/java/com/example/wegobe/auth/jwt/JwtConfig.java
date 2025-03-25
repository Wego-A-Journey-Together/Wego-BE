package com.example.wegobe.auth.jwt;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//application.yml에서 JWT 관련 설정값들을 읽어와 JwtUtil에 전달
@Configuration // Spring 환경설정 클래스임을 명시
public class JwtConfig {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.access.expiration}") // 액세스 토큰 유효기간
    private long accessTokenExpiration;

    @Value("${jwt.token.refresh.expiration}") // 리프레시 토큰 유효기간
    private long refreshTokenExpiration;

    public String getSecretKey() {
        return secretKey;
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    @PostConstruct
    public void init() {
        // 애플리케이션 실행 시 JwtUtil에 설정값 전달
        JwtUtil.init(this);
    }
}

