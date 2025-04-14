package com.example.wegobe.like.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.service.UserService;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.dto.response.GatheringListResponseDto;
import com.example.wegobe.gathering.repository.GatheringRepository;
import com.example.wegobe.like.domain.Like;
import com.example.wegobe.like.dto.LikeResponseDto;
import com.example.wegobe.like.repository.LikeRepository;
import com.example.wegobe.gathering.service.GatheringStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final GatheringRepository gatheringRepository;
    private final UserService userService;
    private final GatheringStatsService gatheringStatsService;

    /**
     * 찜 등록 삭제
     * 유저/동행 으로 등록 여부 확인 후 새로 등록 , 이미 등록되어 있을 경우 삭제
     */
    @Transactional
    public LikeResponseDto aboutLike(Long gatheringId) {
        User user = userService.getCurrentUser();

        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new RuntimeException("해당 동행을 찾을 수 없습니다."));

        Optional<Like> existingLike = likeRepository.findByUserAndGathering(user, gathering);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return new LikeResponseDto(false, "찜이 삭제되었습니다.");
        } else {
            likeRepository.save(Like.builder()
                    .user(user)
                    .gathering(gathering)
                    .build());
            return new LikeResponseDto(true, "찜이 저장되었습니다.");
        }
    }

    /**
     * 찜 여부 조회
     * 특정 유저가 찜한 동행인지 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long gatheringId) {
        User user = userService.getCurrentUser();

        return likeRepository.existsByUserAndGathering(user,
                gatheringRepository.findById(gatheringId)
                        .orElseThrow(() -> new RuntimeException("해당 동행을 찾을 수 없습니다.")));
    }

    /**
     * 특정 유저가 찜한 동행 목록 조회
     */
    @Transactional(readOnly = true)
    public List<GatheringListResponseDto> getLikedGatherings() {
        User user = userService.getCurrentUser();

        return likeRepository.findByUser(user)
                .stream()
                .map(like -> {
                    Gathering gathering = like.getGathering();
                    int currentParticipants = gatheringStatsService.getCurrentParticipants(gathering);
                    return GatheringListResponseDto.fromEntity(gathering, currentParticipants);
                })
                .collect(Collectors.toList());
    }
}