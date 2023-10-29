package com.bigbro.wwooss.v1.repository.trash.can.custom;

import com.bigbro.wwooss.v1.domain.entity.trash.can.TrashCanHistory;
import com.bigbro.wwooss.v1.domain.entity.user.User;
import com.bigbro.wwooss.v1.util.PagingUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Objects;

import static com.bigbro.wwooss.v1.domain.entity.trash.can.QTrashCanHistory.trashCanHistory;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCanHistoryRepositoryImpl implements TrashCanHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<TrashCanHistory> findTrashCanHistoriesByUserAndDate(User user, String date, Pageable pageable) {

        JPAQuery<TrashCanHistory> trashCanHistoryJPAQuery = queryFactory.selectFrom(trashCanHistory)
                .where(searchDateFilter(date))
                .orderBy(trashCanHistory.createdAt.desc());

        return PagingUtil.getSlice(trashCanHistoryJPAQuery, pageable);
    }

    private BooleanExpression searchDateFilter(String date) {
        if (Objects.isNull(date)) return null;

        String[] splitDate = date.split("-");

        return splitDate.length == 1 ? searchYear((Integer.parseInt(splitDate[0])))
                : searchMonthAndDay(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]));
    }

    private BooleanExpression searchYear(int year) {
        return trashCanHistory.createdAt.year().eq(year);
    }

    private BooleanExpression searchMonthAndDay(int year, int month) {
        return trashCanHistory.createdAt.year().eq(year).and(trashCanHistory.createdAt.month().eq(month));
    }

}
