package com.example.wegobe.gathering.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatheringStatus {
    APPLYING,
    BLOCKED,
    ACCEPTED,
}