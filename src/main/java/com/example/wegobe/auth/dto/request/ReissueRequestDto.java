package com.example.wegobe.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// RefreshToken으로 요청
@Getter
@NoArgsConstructor
public class ReissueRequestDto {
    private String refreshToken;
}

