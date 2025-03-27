package com.example.wegobe.gathering.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gathering_id")
    private Gathering gathering;

    @Builder
    public HashTag(String tag, Gathering gathering) {
        this.tag = tag;
        this.gathering = gathering;
    }
}