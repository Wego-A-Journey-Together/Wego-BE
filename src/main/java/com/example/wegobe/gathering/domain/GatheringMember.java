package com.example.wegobe.gathering.domain;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.enums.GatheringStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringMember {

    @Id
    @Column(name = "gathering_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = false)
    private Gathering gathering;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GatheringStatus status;

    @Builder
    public GatheringMember(User user, Gathering gathering, GatheringStatus status) {
        this.user = user;
        this.gathering = gathering;
        this.status = status;
    }
    // 신청 수락
    public void accept() {
        this.status = GatheringStatus.ACCEPTED;
    }
    // 수락 취소
    public void cancelByHost() {
        this.status = GatheringStatus.BLOCKED;
    }
}
