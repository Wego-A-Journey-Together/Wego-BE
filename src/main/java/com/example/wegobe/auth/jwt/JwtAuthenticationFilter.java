package com.example.wegobe.auth.jwt;

import com.example.wegobe.auth.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService; // 사용자 정보 로딩용

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 요청 헤더에서 Authorization 추출
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Bearer 토큰 형식이면 JWT 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = JwtUtil.getUsernameFromToken(jwt);
        }

        // 인증이 필요한 상태라면
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DB에서 사용자 정보 로딩
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 토큰이 유효하다면
            if (JwtUtil.validateToken(jwt, userDetails)) {
                List authorities = JwtUtil.getRolesFromToken(jwt);

                // Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                // 인증 세부 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 현재 요청에 대한 인증 정보를 SecurityContext에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }
}
