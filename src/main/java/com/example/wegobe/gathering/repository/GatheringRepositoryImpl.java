package com.example.wegobe.gathering.repository;

import com.example.wegobe.gathering.domain.Gathering;
import com.example.wegobe.gathering.domain.QGathering;
import com.example.wegobe.gathering.domain.enums.AgeGroup;
import com.example.wegobe.gathering.domain.enums.Category;
import com.example.wegobe.gathering.domain.enums.Gender;
import com.example.wegobe.gathering.dto.request.GatheringFilterRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class GatheringRepositoryImpl implements GatheringRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QGathering g = QGathering.gathering;

    @Override
    public Page<Gathering> findByFilters(GatheringFilterRequestDto filter, Pageable pageable) {
        QGathering g = QGathering.gathering;

        // 조건에 맞는 데이터 목록 조회 (주소, 시작일이후 종료일이전, 카테로그일치, 인원수 이상, 성별일치, 연령대일치,최신순 정렬, 페이지시작,크기)
        List<Gathering> content = queryFactory.selectFrom(g)
                .where(
                        addressContains(filter.getAddress()),
                        startDateAfter(filter.getStartDate()),
                        endDateBefore(filter.getEndDate()),
                        categoryEq(filter.getCategory()),
                        maxParticipantsGte(filter.getMaxParticipants()),
                        genderEq(filter.getPreferredGender()),
                        ageEq(filter.getPreferredAge())
                )
                .orderBy(g.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 페이지 네이션 용
        long total = queryFactory.selectFrom(g)
                .where(
                        addressContains(filter.getAddress()),
                        startDateAfter(filter.getStartDate()),
                        endDateBefore(filter.getEndDate()),
                        categoryEq(filter.getCategory()),
                        maxParticipantsGte(filter.getMaxParticipants()),
                        genderEq(filter.getPreferredGender()),
                        ageEq(filter.getPreferredAge())
                )
                .fetchCount();

        return new PageImpl<>(content, pageable, total);

    }
    // 헬퍼 메서드들
    private BooleanExpression addressContains(String address) {
        return (address == null || address.trim().isEmpty()) ? null : g.location.containsIgnoreCase(address);
    }

    private BooleanExpression startDateAfter(LocalDate startDate) {
        return (startDate == null) ? null : g.startAt.goe(startDate);
    }

    private BooleanExpression endDateBefore(LocalDate endDate) {
        return (endDate == null) ? null : g.endAt.loe(endDate);
    }

    private BooleanExpression categoryEq(Category category) {
        return (category == null) ? null : g.category.eq(category);
    }

    private BooleanExpression maxParticipantsGte(Integer max) {
        return (max == null) ? null : g.maxParticipants.goe(max);
    }

    private BooleanExpression genderEq(Gender gender) {
        return (gender == null) ? null : g.preferredGender.eq(gender);
    }

    private BooleanExpression ageEq(AgeGroup ageGroup) {
        return (ageGroup == null) ? null : g.preferredAge.eq(ageGroup);
    }
}
