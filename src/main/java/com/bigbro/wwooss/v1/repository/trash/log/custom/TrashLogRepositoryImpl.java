package com.bigbro.wwooss.v1.repository.trash.log.custom;

import com.bigbro.wwooss.v1.util.PagingUtil;
import com.bigbro.wwooss.v1.domain.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

import static com.bigbro.wwooss.v1.domain.entity.trash.log.QTrashLog.trashLog;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashLogRepositoryImpl implements TrashLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<TrashLog> findByUserAndDateBetweenOneMonth(User user, LocalDateTime date, Pageable pageable) {

        JPAQuery<TrashLog> trashLogJPAQuery = queryFactory.selectFrom(trashLog)
                .where(searchDateFilter(date))
                .orderBy(trashLog.createdAt.desc());

        return PagingUtil.getSlice(trashLogJPAQuery, pageable);
    }

    private BooleanExpression searchDateFilter(LocalDateTime date) {
        if (Objects.isNull(date)) return null;

        return trashLog.createdAt.between(date.with(TemporalAdjusters.firstDayOfMonth()), date.with(TemporalAdjusters.lastDayOfMonth()));
    }

}
