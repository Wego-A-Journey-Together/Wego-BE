package com.example.wegobe.gathering.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.HashTag;
import com.example.wegobe.gathering.dto.request.GatheringFilterRequestDto;
import com.example.wegobe.gathering.dto.request.GatheringRequestDto;
import com.example.wegobe.gathering.dto.response.GatheringListResponseDto;
import com.example.wegobe.gathering.dto.response.GatheringResponseDto;
import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.gathering.repository.GatheringRepository;
import com.example.wegobe.global.paging.PageableService;
import com.example.wegobe.profile.UserProfileDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringService implements PageableService<Gathering, GatheringListResponseDto> {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final GatheringStatsService gatheringStatsService;

    @Value("${thumbnail.url}")
    private String[] DEFAULT_THUMBNAIL_URLS;

    /**
     * 모임 생성
     */
    @Transactional
    public Long createGathering(GatheringRequestDto requestDto) {

        Long kakaoId = SecurityUtil.getCurrentKakaoId(); // 유저 정보 조회

        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        String thumbnailUrl = getThumbnailUrlOrDefault(requestDto.getThumbnailUrl());

        List<HashTag> hashtags = requestDto.getHashtags().stream()
                .map(tag -> HashTag.builder().tag(tag).build())
                .collect(Collectors.toList());

        Gathering gathering = Gathering.builder()
                .creator(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getEndAt())
                .closedAt(requestDto.getClosedAt())
                .location(requestDto.getAddress())
                .latitude(requestDto.getLatitude())
                .longitude(requestDto.getLongitude())
                .maxParticipants(requestDto.getMaxParticipants())
                .thumbnailUrl(thumbnailUrl)
                .preferredAge(requestDto.getPreferredAgeGroup())
                .preferredGender(requestDto.getPreferredGender())
                .category(requestDto.getCategory())
                .hashtags(hashtags)
                .build();

        gatheringRepository.save(gathering);
        return gathering.getId();
    }

    /**
     * 모임 단일 조회
     */
    @Transactional
    public GatheringResponseDto getGatheringById(Long id) {
        Gathering gathering = gatheringRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 동행을 찾을 수 없습니다."));

        int acceptedCount = gatheringStatsService.getCurrentParticipants(gathering);              // 참여자 수
        int likeCount = gatheringStatsService.getLikeCount(gathering);         // 좋아요 수
        int reviewCount = gatheringStatsService.getReviewCount(gathering);     // 소감 개수

        User creator = gathering.getCreator();
        Double averageRating = gatheringStatsService.getAverageRatingByKakaoId(creator.getKakaoId());
        Long totalReviews = gatheringStatsService.getReviewCountByKakaoId(creator.getKakaoId());

        UserProfileDto creatorProfile = UserProfileDto.fromEntity(creator, averageRating, totalReviews);

        return GatheringResponseDto.fromEntity(gathering, acceptedCount, likeCount, reviewCount, creatorProfile);
    }

    /**
     * 모든 모임 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GatheringListResponseDto> findAll(Pageable pageable) {
        return gatheringRepository.findAll(pageable)
                .map(gathering -> GatheringListResponseDto.fromEntity(gathering, gatheringStatsService.getCurrentParticipants(gathering)));
    }

    /**
     * 모임 수정
     */
    @Transactional
    public GatheringResponseDto updateGathering(Long gatheringId, GatheringRequestDto updateDto) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new RuntimeException("해당 동행을 찾을 수 없습니다."));

        // 수정 권한 체크
        if (!gathering.getCreator().getId().equals(user.getId())) {
            throw new RuntimeException("본인이 작성한 동행만 수정할 수 있습니다.");
        }

        // 썸네일 삭제될 경우 기본 이미지 지정 로직 추가
        String thumbnailUrl = updateDto.getThumbnailUrl();
        if (thumbnailUrl == null || thumbnailUrl.trim().isEmpty()) {
            thumbnailUrl = getThumbnailUrlOrDefault(thumbnailUrl);  // 기본 이미지 URL 적용
        }

        gathering.update(
                updateDto.getTitle(),
                updateDto.getContent(),
                updateDto.getAddress(),
                updateDto.getLatitude(),
                updateDto.getLongitude(),
                thumbnailUrl,
                updateDto.getMaxParticipants(),
                updateDto.getStartAt(),
                updateDto.getEndAt(),
                updateDto.getClosedAt(),
                updateDto.getPreferredGender(),
                updateDto.getPreferredAgeGroup(),
                updateDto.getCategory()
        );

        updateHashtags(gathering, updateDto.getHashtags()); // 해시태그 별도 update

        int acceptedCount = gatheringStatsService.getCurrentParticipants(gathering);
        int likeCount = gatheringStatsService.getLikeCount(gathering);
        int reviewCount = gatheringStatsService.getReviewCount(gathering);

        // ✅ 주최자 평점 통계 포함된 UserProfileDto 생성
        User creator = gathering.getCreator();
        Double averageRating = gatheringStatsService.getAverageRatingByKakaoId(creator.getKakaoId());
        Long totalReviews = gatheringStatsService.getReviewCountByKakaoId(creator.getKakaoId());
        UserProfileDto creatorProfile = UserProfileDto.fromEntity(creator, averageRating, totalReviews);

        return GatheringResponseDto.fromEntity(gathering, acceptedCount,likeCount, reviewCount, creatorProfile);
    }

    /**
     * 모임 삭제
     */
    public void deleteGathering(Long id) {
        Gathering gathering = gatheringRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gathering not found"));
        gatheringRepository.delete(gathering);
    }

    /**
     * 내가 등록한 동행 조회
     */
    public Page<GatheringSimpleResponseDto> getMyGatherings(Pageable pageable) {
        Long kakaoId = SecurityUtil.getCurrentKakaoId();
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return gatheringRepository.findByCreator(user, pageable)
                .map(gathering -> GatheringSimpleResponseDto.fromEntity(gathering, gatheringStatsService.getCurrentParticipants(gathering)));
    }

    /**
     * 썸네일이 없을 경우 기본 이미지 URL 지정
     * 랜덤한 기본 이미지 URL 반환
     */
    private String getThumbnailUrlOrDefault(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.trim().isEmpty()) {
            return getRandomDefaultThumbnail();
        }
        return thumbnailUrl;
    }

    /**
     * 기본 이미지 URL 중 하나를 랜덤으로 선택
     */
    private String getRandomDefaultThumbnail() {
        int randomIndex = ThreadLocalRandom.current().nextInt(DEFAULT_THUMBNAIL_URLS.length);  // 배열 길이 확인
        return DEFAULT_THUMBNAIL_URLS[randomIndex];
    }


    /**
     * 동행 수정 시, 해시태그 비교 메서드
     * 기존 해시태그와 변경된 해시태그만 비교하여 update
     */
    private void updateHashtags(Gathering gathering, List<String> newHashtagNames) {
        if (newHashtagNames == null) return;

        // 현재 해시태그 리스트 문자열 추출
        Set<String> currentTagNames = gathering.getHashtags().stream()
                .map(HashTag::getTag)
                .collect(Collectors.toSet());

        Set<String> newTagNames = new HashSet<>(newHashtagNames);

        // 삭제할 해시태그 찾기
        List<HashTag> tagsToRemove = gathering.getHashtags().stream()
                .filter(tag -> !newTagNames.contains(tag.getTag()))
                .collect(Collectors.toList());

        // 추가할 해시태그 찾기
        List<HashTag> tagsToAdd = newHashtagNames.stream()
                .filter(tag -> !currentTagNames.contains(tag))
                .map(tag -> new HashTag(tag, gathering))
                .collect(Collectors.toList());

        gathering.getHashtags().removeAll(tagsToRemove);
        gathering.getHashtags().addAll(tagsToAdd);
    }

    // 검색하기
    @Transactional(readOnly = true)
    public Page<GatheringListResponseDto> searchByKeyword(String keyword, Pageable pageable) {
        return gatheringRepository.searchByTitleOrHashtag(keyword, pageable)
                .map(gathering -> GatheringListResponseDto.fromEntity(gathering, gatheringStatsService.getCurrentParticipants(gathering)));
    }

    // 동행 필터링하기
    @Transactional(readOnly = true)
    public Page<GatheringListResponseDto> filterGatherings(GatheringFilterRequestDto filter, Pageable pageable) {
        return gatheringRepository.findByFilters(filter, pageable)
                .map(gathering -> GatheringListResponseDto.fromEntity(gathering, gatheringStatsService.getCurrentParticipants(gathering)));
    }
}