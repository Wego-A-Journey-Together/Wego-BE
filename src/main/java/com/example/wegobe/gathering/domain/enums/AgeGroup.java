package com.example.wegobe.gathering.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeGroup {
    ALL("나이무관",0,100),
    TEENS("10대", 10, 19),
    TWENTIES("20대", 20, 29),
    THIRTIES("30대", 30, 39),
    FORTIES("40대", 40, 49),
    FIFTIES("50대", 50, 59),
    SIXTIES("60대",60, 69),
    SEVENTIES("70대",70,79);

    private final String description;
    private final int minAge;
    private final int maxAge;
}