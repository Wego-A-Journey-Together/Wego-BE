package com.example.wegobe.gathering.domain;

import com.example.wegobe.auth.entity.User;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Category;
import com.example.wegobe.gathering.domain.enums.Gender;
import com.example.wegobe.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering extends BaseTimeEntity {

    @Id
    @Column(name = "gathering_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String thumbnailUrl;

    private Integer maxParticipants;    // 최대 인원

    private LocalDate startAt;          // 동행 시작 날짜 (년월일시간분)

    private LocalDate endAt;

    private LocalDateTime closedAt;     // 모임 모집 마감날짜

    private String location;            // 도로명주소

    @Enumerated(EnumType.STRING)
    private Gender preferredGender;

    @Enumerated(EnumType.STRING)
    private AgeGroup preferredAge;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "gathering_id")
    private List<HashTag> hashtags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator; // 주최자

    @Builder
    public Gathering(String title, String content, String location, String thumbnailUrl, int maxParticipants,
                     LocalDate startAt, LocalDate endAt, LocalDateTime closedAt, Gender preferredGender,
                     AgeGroup preferredAge, Category category, List<HashTag> hashtags, User creator) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.thumbnailUrl = thumbnailUrl;
        this.maxParticipants = maxParticipants;
        this.startAt = startAt;
        this.endAt = endAt;
        this.closedAt = closedAt;
        this.preferredGender = preferredGender;
        this.preferredAge = preferredAge;
        this.category = category;
        this.hashtags = hashtags;
        this.creator = creator;
    }

    public void update(
            String title, String content, String location, String thumbnailUrl, int maxParticipants,
            LocalDate startAt, LocalDate endAt, LocalDateTime closedAt, Gender preferredGender,
            AgeGroup preferredAge, Category category) {

        this.title = title;
        this.content = content;
        this.location = location;
        this.thumbnailUrl = thumbnailUrl;
        this.maxParticipants = maxParticipants;
        this.startAt = startAt;
        this.endAt = endAt;
        this.closedAt = closedAt;
        this.preferredGender = preferredGender;
        this.preferredAge = preferredAge;
        this.category = category;

    }
}