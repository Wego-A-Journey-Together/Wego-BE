package com.example.wegobe.gathering.service;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.auth.repository.UserRepository;
import com.example.wegobe.auth.service.UserService;
import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.GatheringMember;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import com.example.wegobe.gathering.dto.response.GatheringMemberResponseDto;
import com.example.wegobe.gathering.dto.response.GatheringSimpleResponseDto;
import com.example.wegobe.gathering.repository.GatheringMemberRepository;
import com.example.wegobe.gathering.repository.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringMemberService {

    private final GatheringMemberRepository gatheringMemberRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final UserService userService;

    // 동행 참여 신청
    public void applyGathering(Long gatheringId) {
        // 1. 유저 정보 조회
        User user = userService.getCurrentUser();
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
    public void cancelApplying(Long gatheringId) {
        User user = userService.getCurrentUser();
        Gathering gathering = getGatheringById(gatheringId);

        // 동행 참여 신청 내역 확인
        GatheringMember gatheringMember = getMember(user, gathering);

        gatheringMemberRepository.delete(gatheringMember);
    }

    // 동행 신청 수락
    public void acceptApply(Long gatheringId, Long userId) {

        User host = userService.getCurrentUser();
        Gathering gathering = getGatheringById(gatheringId);

        // 동행 주최자만 수락할 수 있으므로 권한 확인
        validateHost(gathering, host);
        // 동행 참여 신청자 조회
        User participant = getUserById(userId);

        // 동행 참여 신청 내역 확인
        GatheringMember gatheringMember = getMember(participant, gathering);
        // 동행 참여 신청 상태 변경 Applying -> accepted
        gatheringMember.accept();

    }

    // 수락된 동행 취소 (삭제 X, 상태 변경)
    public void cancelParticipator(Long gatheringId, Long userId) {
        User host = userService.getCurrentUser();
        Gathering gathering = getGatheringById(gatheringId);

        validateHost(gathering, host);

        User participant = getUserById(userId);
        GatheringMember gatheringMember = getMember(participant, gathering);

        // 동행 참여 신청 상태 변경 accepted -> blocked
        gatheringMember.cancelByHost();

    }

    // 주최자가 동행 신청한 유저 목록 조회
    public List<GatheringMemberResponseDto> getAppliersList(Long gatheringId) {

        User host = userService.getCurrentUser();
        Gathering gathering = getGatheringById(gatheringId);

        validateHost(gathering, host);

        return gatheringMemberRepository.findByGathering(gathering)
                .stream()
                .map(GatheringMemberResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 동행에 참여 확정된 유저 조회 (수락된 유저들 목록)
    public List<GatheringMemberResponseDto> getParticipantsList(Long gatheringId) {
        Gathering gathering = getGatheringById(gatheringId);

        return gatheringMemberRepository.findByGatheringAndStatus(gathering, GatheringStatus.ACCEPTED)
                .stream()
                .map(GatheringMemberResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 동행 신청이 받아들여진 즉, 내가 참여 중인 동행 목록 조회
    public List<GatheringSimpleResponseDto> getMyJoinedGatherings() {
        User user = userService.getCurrentUser();

        return gatheringMemberRepository.findByUserAndStatus(user, GatheringStatus.ACCEPTED)
                .stream()
                .map(member -> GatheringSimpleResponseDto.fromEntity(member.getGathering()))
                .collect(Collectors.toList());
    }

    /**
     * 유틸 메서드 분리
     */

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

    // 신청하는 유저의 정보 조회
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // host 권한 조회
    private void validateHost(Gathering gathering, User host) {
        if (!gathering.getCreator().equals(host)) {
            throw new RuntimeException("동행 생성자만 이 작업을 수행할 수 있습니다.");
        }
    }
}
