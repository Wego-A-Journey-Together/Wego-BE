package com.example.wegobe.gathering.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.config.SecurityUtil;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.HashTag;
import com.example.wegobe.gathering.dto.request.GatheringRequestDto;
import com.example.wegobe.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final String DEFAULT_THUMBNAIL_URL = "/resources/picture.jpeg";  // 임시 테스트 이미지

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
     * 썸네일이 없을 경우 기본 이미지 URL 반환
     * 이미지 생성되면 해당 이미지 URL로 변환 예정
      */
    private String getThumbnailUrlOrDefault(String thumbnailUrl) {
        return (thumbnailUrl != null && !thumbnailUrl.isEmpty()) ? thumbnailUrl : DEFAULT_THUMBNAIL_URL;
    }

}

