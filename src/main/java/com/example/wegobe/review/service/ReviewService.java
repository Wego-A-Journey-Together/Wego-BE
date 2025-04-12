package com.example.wegobe.review.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.service.UserService;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.GatheringMember;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.gathering.repository.GatheringMemberRepository;
import com.example.wegobe.gathering.repository.GatheringRepository;
import com.example.wegobe.gathering.service.GatheringService;
import com.example.wegobe.profile.WriterProfileDto;
import com.example.wegobe.review.domain.Review;
import com.example.wegobe.review.dto.MyReviewResponseDto;
import com.example.wegobe.review.dto.ReviewRequestDto;
import com.example.wegobe.review.dto.ReviewResponseDto;
import com.example.wegobe.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserService userService;
    private final GatheringService gatheringService;

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

    /**
     * 동행별 리뷰 조회 (최신순)
     */
    public Page<ReviewResponseDto> getReviewsByGathering(Long gatheringId, Pageable pageable) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동행입니다."));
        return reviewRepository.findAllByGatheringOrderByCreatedDateDesc(gathering, pageable)
                .map(ReviewResponseDto::fromEntity);
    }

    /**
     * 내가 작성한 리뷰 목록
     */
    public Page<MyReviewResponseDto> getMyReviews(Pageable pageable) {
        User user = userService.getCurrentUser();

        return reviewRepository.findAllByWriterOrderByCreatedDateDesc(user, pageable)
                .map(review -> {
                    Gathering gathering = review.getGathering();
                    int currentParticipants = gatheringService.getCurrentParticipants(gathering);
                    return MyReviewResponseDto.fromEntity(review, currentParticipants);
                });
    }

    /**
     * 내가 주최한 동행에 대한 리뷰 목록
     */
    public Page<ReviewResponseDto> getReviewsForMyGatherings(Pageable pageable) {
        User user = userService.getCurrentUser();
        return reviewRepository.findAllByGathering_CreatorOrderByCreatedDateDesc(user, pageable)
                .map(ReviewResponseDto::fromEntity);
    }

    /**
     * 내가 참여한 동행 중 리뷰 안 쓴 동행 목록
     */
    public List<GatheringSimpleResponseDto> getUnwrittenReviewGatherings() {
        User user = userService.getCurrentUser();

        // 참여한 동행 중 ACCEPTED 상태
        List<GatheringMember> participated = gatheringMemberRepository.findByUserAndStatus(user, GatheringStatus.ACCEPTED);

        return participated.stream()
                .map(GatheringMember::getGathering)
                .filter(gathering -> reviewRepository.findByWriterAndGathering(user, gathering).isEmpty())
                .map(gathering -> {
                    int currentParticipants = gatheringService.getCurrentParticipants(gathering);
                    WriterProfileDto host = WriterProfileDto.fromEntity(gathering.getCreator());
                    return GatheringSimpleResponseDto.fromEntity(gathering, currentParticipants, host);
                })
                .collect(Collectors.toList());
    }
}