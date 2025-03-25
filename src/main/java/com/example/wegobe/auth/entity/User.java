package com.example.wegobe.auth.entity;

import com.example.wegobe.config.redis.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 (null 허용 가능 - Kakao 로그인 대비)
    @Column(unique = true)
    private String email;

    private String password;

    private String username;

    private Long kakaoId; // 카카오 로그인 시 필요한 kakao 고유 ID

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String profileImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRole> roles = new ArrayList<>();

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}

