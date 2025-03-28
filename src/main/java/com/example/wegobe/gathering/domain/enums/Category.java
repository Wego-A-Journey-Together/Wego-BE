package com.example.wegobe.gathering.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    PART("부분동행"),
    TOUR("투어 동행"),
    SHARE("숙박 공유"),
    SHOW("전시/공연 동행"),
    RESTAURANT("맛집 동행");

    private final String description;
}