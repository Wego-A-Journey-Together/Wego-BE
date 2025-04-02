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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth API", description = "카카오 로그인 및 사용자 인증 관련 API")
public class AuthController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Operation(summary = "카카오 로그인", description = "인가 코드(code)로 로그인 정보를 전달")
    @PostMapping("/kakao/callback")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody KakaoCodeRequest request) {
        String code = request.getCode();
        LoginResponse response = kakaoService.kakaoLogin(code);
        return ResponseEntity.ok(response);
    }



    // 액세스토큰 재발급
    @Operation(summary = "액세스 토큰 재발급", description = "기존 액세스 토큰과 리프레시 토큰으로 새로운 액세스 토큰을 발급")
    @PostMapping("/reissue")
    public ResponseEntity<AccessTokenResponseDto> reissue(@RequestBody ReissueRequestDto request, HttpServletRequest httpRequest) {
        String accessToken = jwtUtil.resolveToken(httpRequest);
        String kakaoId = jwtUtil.getUsernameFromToken(accessToken);
        redisService.validateRefreshToken(kakaoId, request.getRefreshToken());
        String newAccessToken = jwtUtil.createAccessToken(kakaoId, List.of("USER"));
        return ResponseEntity.ok(new AccessTokenResponseDto(newAccessToken));
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "Redis에서 RefreshToken을 제거해서 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        String kakaoId = jwtUtil.getUsernameFromToken(token);
        redisService.deleteRefreshToken(kakaoId);
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }

    // 내 정보 조회
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회")
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
