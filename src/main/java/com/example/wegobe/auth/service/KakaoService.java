package com.example.wegobe.auth.service;

import com.example.wegobe.auth.dto.response.KakaoTokenResponse;
import com.example.wegobe.auth.dto.response.KakaoUserInfoResponse;
import com.example.wegobe.auth.dto.response.LoginResponse;
import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.jwt.JwtUtil;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.config.client.KakaoOAuthClient;
import com.example.wegobe.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * 1. 인가 코드(code) → 카카오 accessToken 요청
 * 2. accessToken → 사용자 정보 요청
 * 3. 사용자 정보로 DB 확인
 *      없으면 회원가입
 *      있으면 로그인 처리
 * 4. JWT AccessToken + RefreshToken 생성
 * 5. Redis에 RefreshToken 저장
 * 6. LoginResponseDto로 클라이언트에 응답
 */
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final RedisService redisService;

    //  Kakao 로그인
    public LoginResponse kakaoLogin(String code) {
        // 1. 인가코드로 accessToken 받기
        KakaoTokenResponse tokenResponse = kakaoOAuthClient.requestAccessToken(code);
        String kakaoAccessToken = tokenResponse.getAccess_token();


        // 2. accessToken으로 사용자 정보 가져오기
        KakaoUserInfoResponse userInfo = kakaoOAuthClient.requestUserInfo(kakaoAccessToken);
        Long kakaoId = userInfo.getId();

        // 이메일 없을 경우, kakaoId 기반 가짜 이메일 생성
        String rawEmail = userInfo.getKakao_account().getEmail();
        final String email = (rawEmail == null || rawEmail.isEmpty())
                ? kakaoId + "@wego.com"
                : rawEmail;        String nickname = userInfo.getKakao_account().getProfile().getNickname();

        // 3. DB에 사용자 존재 여부 확인
        Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
        User user = optionalUser.orElseGet(() ->
                userRepository.save(User.builder()
                        .kakaoId(kakaoId)
                        .email(email)
                        .nickname(nickname)
                        .build())
        );

        // 4. JWT subject로 이메일이 없으면 kakaoId 사용
        String subject =  String.valueOf(kakaoId);

        // 5. JWT 발급
        String accessToken = JwtUtil.createAccessToken(subject, Collections.singletonList("USER"));
        String refreshToken = JwtUtil.createRefreshToken(subject, Collections.singletonList("USER"));

        // 6. Redis에 refreshToken 저장
        redisService.saveRefreshToken(subject, refreshToken);

        // 7. 클라이언트에 응답
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .nickname(nickname)
                .kakaoId(kakaoId)
                .build();
    }
}

