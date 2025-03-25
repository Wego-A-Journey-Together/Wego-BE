package com.example.wegobe.auth.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 권한 정보를 Spring Security의 GrantedAuthority로 변경
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
}
