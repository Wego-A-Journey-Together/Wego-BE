package com.example.wegobe.gathering.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("남자"),
    FEMALE("여자"),
    ANY("남녀무관");

    private final String description;
}