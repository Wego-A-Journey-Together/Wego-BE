package com.example.wegobe.gathering.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FRIEND("친구 동행"),
    FAMILY("부부 동행"),
    TOUR("투어 동행"),
    SHARE("숙박 공유"),
    SHOW("전시/공연 동행"),
    RESTAURANT("맛집 동행");

    private final String description;
}