package com.example.wegobe.gathering.service;

import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import com.example.wegobe.gathering.repository.GatheringMemberRepository;
import com.example.wegobe.like.repository.LikeRepository;
import com.example.wegobe.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringStatsService {

    private final ReviewRepository reviewRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final LikeRepository likeRepository;

    // 동행 참여자 수 조회
    public int getCurrentParticipants(Gathering gathering) {
        return gatheringMemberRepository.countByGatheringAndStatus(gathering, GatheringStatus.ACCEPTED);
    }
    // 동행에 달린 좋아요 수 조회
    public int getLikeCount(Gathering gathering) {
        return likeRepository.countByGathering(gathering);
    }
    // 동행에 달린 소감 수 조회
    public int getReviewCount(Gathering gathering) {
        return reviewRepository.countByGathering(gathering);
    }
    // 유저 평점 조회
    public Double getAverageRatingByKakaoId(Long kakaoId) {
        return reviewRepository.findAverageRatingByKakaoId(kakaoId);
    }
    // 유저 소감 수 조회
    public Long getReviewCountByKakaoId(Long kakaoId) {
        return reviewRepository.countByKakaoId(kakaoId);
    }
}
