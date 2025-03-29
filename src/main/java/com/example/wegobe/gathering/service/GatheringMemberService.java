package com.example.wegobe.gathering.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.GatheringMember;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import com.example.wegobe.gathering.repository.GatheringMemberRepository;
import com.example.wegobe.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringMemberService {

    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;

    // 동행 참여 신청
    public void applyGathering(Long gatheringId, Long kakaoId) {
        // 1. 유저 정보 조회
        User user = getUserByKakaoId(kakaoId);
        // 2. 동행 아이디로 동행 조회
        Gathering gathering = getGatheringById(gatheringId);

        // 3.동행 참여 신청 내역 확인
        Optional<GatheringMember> existing = gatheringMemberRepository.findByUserAndGathering(user, gathering);
        if (existing.isPresent()) {

            GatheringMember participant = existing.get();
            GatheringStatus currentStatus = participant.getStatus();

            if (currentStatus == GatheringStatus.APPLYING || currentStatus == GatheringStatus.ACCEPTED) {
                throw new IllegalStateException("이미 신청한 동행입니다.");
            }
            if (currentStatus == GatheringStatus.BLOCKED) {
                throw new IllegalStateException("해당 동행을 신청할 수 없습니다.");
            }
        }
        // 4. 동행 신청
        GatheringMember gatheringMember = GatheringMember.builder()
                .user(user)
                .gathering(gathering)
                .status(GatheringStatus.APPLYING)
                .build();

        gatheringMemberRepository.save(gatheringMember);
    }


    // 동행 참여 신청 취소
    public void cancelApplying(Long gatheringId, Long kakaoId) {
        User user = getUserByKakaoId(kakaoId);
        Gathering gathering = getGatheringById(gatheringId);

        // 동행 참여 신청 내역 확인
        GatheringMember gatheringMember = getMember(user, gathering);

        gatheringMemberRepository.delete(gatheringMember);
    }




    /**
     * 유틸 메서드 분리
     */
    // 사용자 인증 정보 조회
    private User getUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new RuntimeException("인증된 사용자가 아닙니다."));
    }

    // 동행 정보 조회
    private Gathering getGatheringById(Long gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new RuntimeException("해당 동행을 찾을 수 없습니다."));
    }

    // 유저의 신청 기록 조회
    private GatheringMember getMember(User user, Gathering gathering) {
        return gatheringMemberRepository.findByUserAndGathering(user, gathering)
                .orElseThrow(() -> new RuntimeException("신청 기록이 없습니다."));
    }
}
