package com.example.wegobe.auth.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * 권한 테이블
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
