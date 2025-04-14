package com.example.wegobe.profile.controller;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.service.UserService;
import com.example.wegobe.profile.ProfileDto;
import com.example.wegobe.profile.UserProfileDto;
import com.example.wegobe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;
    private final ReviewService reviewService;


    @Value("${thumbnail.url}")
    private String DEFAULT_THUMBNAIL_URL;

//    @Tag(name = "Profile", description = "내 프로필 가져하기")
//
//    @GetMapping("/me")
//    public ResponseEntity<ProfileDto> getMyProfile() {
//        User user = userService.getCurrentUser();
//
//        return ResponseEntity.ok(
//                ProfileDto.builder()
//                        .nickname(user.getNickname())
//                        .email(user.getEmail())
//                        .statusMessage(user.getStatusMessage())
//                        .thumbnailUrl(user.getThumbnailUrl() != null ? user.getThumbnailUrl() : DEFAULT_THUMBNAIL_URL)
//                        .gender(user.getGender())
//                        .ageGroup(user.getAgeGroup())
//                        .build()
//        );
//    }
    @Tag(name = "Profile", description = "내 프로필 추가/수정하기")

    @PutMapping("/me")
    public ResponseEntity<String> updateMyProfile(@RequestBody ProfileDto dto) {
        String finalThumbnail = (dto.getThumbnailUrl() == null || dto.getThumbnailUrl().isBlank())
                ? DEFAULT_THUMBNAIL_URL : dto.getThumbnailUrl();
        ProfileDto fixed = dto.toBuilder().thumbnailUrl(finalThumbnail).build();

        userService.updateProfile(fixed);
        return ResponseEntity.status(HttpStatus.OK).body("프로필이 성공적으로 수정되었습니다.");

    }
    @Tag(name = "Profile", description = "다른 유저 프로필 조회")
    @GetMapping("/{kakaoId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long kakaoId) {
        User user = userService.getUserByKakaoId(kakaoId);
        Double avgRating = reviewService.getAverageRatingByKakaoId(kakaoId);
        Long totalReviews = reviewService.getReviewCountByKakaoId(kakaoId);
        return ResponseEntity.ok(UserProfileDto.fromEntity(user, avgRating, totalReviews));
    }
}

