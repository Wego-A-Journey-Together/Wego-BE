package com.example.wegobe.review.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.service.UserService;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import com.example.wegobe.gathering.repository.GatheringMemberRepository;
import com.example.wegobe.gathering.repository.GatheringRepository;
import com.example.wegobe.review.domain.Review;
import com.example.wegobe.review.dto.ReviewRequestDto;
import com.example.wegobe.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserService userService;

    /**
     * 리뷰 등록
     * 동행에 참여한 적 없거나 이미 리뷰 작성 시 리뷰 등록 불가
     */
    public Long writeReview(Long gatheringId, ReviewRequestDto requestDto) {
        User writer = userService.getCurrentUser();
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동행입니다."));

        // 참여자 여부 검증
        boolean isParticipated = gatheringMemberRepository.existsByUserAndGatheringAndStatus(writer, gathering, GatheringStatus.ACCEPTED);
        if (!isParticipated) {
            throw new IllegalStateException("해당 동행에 참여하지 않았습니다.");
        }

        // 중복 작성 방지
        reviewRepository.findByWriterAndGathering(writer, gathering)
                .ifPresent(r -> { throw new IllegalStateException("이미 리뷰를 작성했습니다."); });

        Review review = Review.builder()
                .writer(writer)
                .gathering(gathering)
                .rating(requestDto.getRating())
                .content(requestDto.getContent())
                .build();

        return reviewRepository.save(review).getId();
    }

}