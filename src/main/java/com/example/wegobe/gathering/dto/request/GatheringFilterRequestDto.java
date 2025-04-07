package com.example.wegobe.gathering.dto.request;


import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Category;
import com.example.wegobe.gathering.domain.enums.Gender;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class GatheringFilterRequestDto {

    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Category category;
    private Integer maxParticipants;
    private Gender preferredGender;
    private AgeGroup preferredAge;
}
