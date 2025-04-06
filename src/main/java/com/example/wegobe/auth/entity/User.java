package com.example.wegobe.auth.entity;

import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId; // 카카오 고유 식별자

    @Column(nullable = false)
    private String email; // 동의 안 했을 수 있으므로 nullable

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    private String statusMessage;
    private String thumbnailUrl;

    public void updateProfile(String nickname, String statusMessage, String thumbnailUrl, Gender gender, AgeGroup ageGroup) {
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.thumbnailUrl = thumbnailUrl;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }
}