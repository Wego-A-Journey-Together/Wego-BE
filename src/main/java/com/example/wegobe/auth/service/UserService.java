package com.example.wegobe.auth.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.profile.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String kakaoIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Long kakaoId = Long.parseLong(kakaoIdStr);
        return userDetailsService.findByKakaoId(kakaoId);
    }

    public void updateProfile(ProfileDto dto) {
        User user = getCurrentUser();
        user.updateProfile(
                dto.getNickname(),
                dto.getStatusMessage(),
                dto.getThumbnailUrl(),
                dto.getGender(),
                dto.getAgeGroup()
        );
        userRepository.save(user);
    }

    public User getUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }
}
