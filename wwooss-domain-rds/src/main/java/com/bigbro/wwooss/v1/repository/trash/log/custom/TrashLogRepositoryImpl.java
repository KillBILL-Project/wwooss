package com.bigbro.wwooss.v1.repository.trash.log.custom;

import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.utils.PagingUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Objects;

import static com.bigbro.wwooss.v1.entity.trash.log.QTrashLog.trashLog;
import static com.bigbro.wwooss.v1.entity.user.QUser.user;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashLogRepositoryImpl implements TrashLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<TrashLog> findByUserAndDate(User user, String date, Pageable pageable) {

        JPAQuery<TrashLog> trashLogJPAQuery = queryFactory.selectFrom(trashLog)
                .where(searchDateFilter(date))
                .orderBy(trashLog.createdAt.desc());

        return PagingUtil.getSlice(trashLogJPAQuery, pageable);
    }

    @Override
    public List<TrashLog> findTrashLogByUserAtLastWeek(User paramUser, LocalDate date) {
        JPAQuery<TrashLog> trashLogJPAQuery = queryFactory.selectFrom(trashLog)
                .where(searchLastWeekDateFilter(date).and(user.userId.eq(paramUser.getUserId())))
                .orderBy(trashLog.createdAt.desc());

        return trashLogJPAQuery.fetch();
    }

    private BooleanExpression searchLastWeekDateFilter(LocalDate date) {
        LocalDate toDate = date.minusDays(1);
        LocalDate fromDate = date.minusDays(7);

        return trashLog.createdAt.between(toDate.atStartOfDay(), fromDate.atTime(LocalTime.MAX));
    }

    private BooleanExpression searchDateFilter(String date) {
        if (Objects.isNull(date)) return null;

        String[] splitDate = date.split("-");

        return splitDate.length == 1 ? searchYear((Integer.parseInt(splitDate[0])))
                : searchMonthAndDay(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]));
    }

    private BooleanExpression searchYear(int year) {
        return trashLog.createdAt.year().eq(year);
    }

    private BooleanExpression searchMonthAndDay(int year, int month) {
        return trashLog.createdAt.year().eq(year).and(trashLog.createdAt.month().eq(month));
    }

}
