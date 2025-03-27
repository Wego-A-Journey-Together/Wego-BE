package com.example.wegobe.auth.controller;

import com.example.wegobe.auth.dto.request.KakaoCodeRequest;
import com.example.wegobe.auth.dto.response.LoginResponse;
import com.example.wegobe.auth.dto.request.ReissueRequestDto;
import com.example.wegobe.auth.dto.response.AccessTokenResponseDto;
import com.example.wegobe.auth.dto.response.UserInfoResponseDto;
import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.jwt.JwtUtil;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.auth.service.KakaoService;
import com.example.wegobe.config.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class AuthController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final UserRepository userRepository;

    // 카카오 로그인 콜백 클라이언트의 인가코드 -> 카카오 정보 불러와서 서버 엑세스 응답
//    @GetMapping("/kakao/callback")
//    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {
//        LoginResponse response = kakaoService.kakaoLogin(code);
//        return ResponseEntity.ok(response); // AccessToken 포함 응답
//    }
    @PostMapping("/kakao/callback")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody KakaoCodeRequest request) {
        String code = request.getCode();
        LoginResponse response = kakaoService.kakaoLogin(code);
        return ResponseEntity.ok(response);
    }



    // 액세스토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenResponseDto> reissue(@RequestBody ReissueRequestDto request, HttpServletRequest httpRequest) {
        String accessToken = jwtUtil.resolveToken(httpRequest);
        String kakaoId = jwtUtil.getUsernameFromToken(accessToken);
        redisService.validateRefreshToken(kakaoId, request.getRefreshToken());
        String newAccessToken = jwtUtil.createAccessToken(kakaoId, List.of("USER"));
        return ResponseEntity.ok(new AccessTokenResponseDto(newAccessToken));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        String kakaoId = jwtUtil.getUsernameFromToken(token);
        redisService.deleteRefreshToken(kakaoId);
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getMyInfo(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        String kakaoId = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByKakaoId(Long.parseLong(kakaoId))
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        return ResponseEntity.ok(UserInfoResponseDto.builder()
                .kakaoId(user.getKakaoId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build());
    }
}
