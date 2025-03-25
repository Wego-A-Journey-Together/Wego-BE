package com.example.wegobe.auth.jwt;


import io.jsonwebtoken.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    // 설정값을 담는 static 변수
    private static String secretKey;
    private static long accessTokenExpiration;
    private static long refreshTokenExpiration;

    private JwtUtil() {} // static-only 유틸이므로 생성자 숨김

    // JwtConfig에서 설정값을 받아 초기화 (Base64 인코딩 포함)
    public static void init(JwtConfig jwtConfig) {
        secretKey = Base64.getEncoder().encodeToString(jwtConfig.getSecretKey().getBytes());
        accessTokenExpiration = jwtConfig.getAccessTokenExpiration();
        refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
    }

    // 액세스 토큰 생성
    public static String createAccessToken(String username, List<String> roles) {
        return generateToken(username, accessTokenExpiration, roles);
    }

    // 리프레시 토큰 생성
    public static String createRefreshToken(String username, List<String> roles) {
        return generateToken(username, refreshTokenExpiration, roles);
    }

    // 실제 토큰 생성 로직 (공통)
    private static String generateToken(String username, long expiration, List<String> roles) {
        return Jwts.builder()
                .setSubject(username) // 토큰 사용자 식별자
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Claims 정보 추출
    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사 (사용자 정보 포함)
    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // 토큰 자체 유효성 검사 (예외 발생 여부로 판단)
    public static boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 만료 여부 검사
    private static boolean isTokenExpired(String token) {
        final Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 사용자 아이디(이메일 등) 추출 -> 아직 미정
    public static String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    // 권한 정보 추출
    public static List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = extractClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
