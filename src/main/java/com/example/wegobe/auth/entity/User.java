package com.example.wegobe.auth.entity;

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

    @Column(nullable = true)
    private String email; // 동의 안 했을 수 있으므로 nullable

    @Column(nullable = false)
    private String nickname;


}