package com.example.wegobe.profile;

import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String nickname;
    private String email;
    private String statusMessage;
    private String thumbnailUrl;
    private Gender gender;
    private AgeGroup ageGroup;
}

