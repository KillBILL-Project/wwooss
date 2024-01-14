package com.bigbro.wwooss.v1.repository.report.custom;

import com.bigbro.wwooss.v1.dto.WeekInfo;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.utils.DateUtil;
import com.bigbro.wwooss.v1.utils.PagingUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import static com.bigbro.wwooss.v1.entity.report.QWeeklyReport.weeklyReport;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyReportRepositoryImpl implements WeeklyReportRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final DateUtil dateUtil;

    @Override
    public WeeklyReport findWeeklyReportByUserAtLastWeek(User user) {
        JPAQuery<WeeklyReport> weeklyReportJPAQuery = jpaQueryFactory.selectFrom(weeklyReport)
                .where(searchLastWeek().and(weeklyReport.user.eq(user)));
        return weeklyReportJPAQuery.fetchFirst();
    }

    @Override
    public Slice<WeeklyReport> findWeeklyReportByUserAtDate(String date, User user, Pageable pageable) {
        JPAQuery<WeeklyReport> weeklyReportJPAQuery = jpaQueryFactory.selectFrom(weeklyReport)
                .where(weeklyReport.user.eq(user).and(searchDate(date)))
                .orderBy(weeklyReport.weeklyDate.desc());

        return PagingUtil.getSlice(weeklyReportJPAQuery, pageable);
    }

    private BooleanExpression searchLastWeek() {
        LocalDate today = LocalDate.now();
        LocalDate lastWeekDate = today.minusDays(7);

        return weeklyReport.weeklyDate.between(lastWeekDate.atStartOfDay(), lastWeekDate.atTime(LocalTime.MAX));
    }

    private BooleanExpression searchDate(String date) {
        if (StringUtils.isBlank(date)) return null;
        String[] splitDate = date.split("-");

        return splitDate.length == 1 ? searchYear(Integer.parseInt(splitDate[0]))
                : searchYearAndMonth(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]));
    }

    private BooleanExpression searchYear(int year) {
        return weeklyReport.weeklyDate.year().eq(year);
    }

    // 날짜가 전달 혹은 다음 달에 포함될 케이스를 고려해 첫주 월요일 ~ 마지막주 일요일로 조회
    // 예) 2023/02/01 (목) => 1월 마지막 주 이지만 2월임.
    private BooleanExpression searchYearAndMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month + 1, calendar.getActualMaximum(Calendar.DATE));
        int lastWeek = calendar.get(Calendar.WEEK_OF_MONTH);

        Calendar firstMonday = dateUtil.getDayAtWeekOfMonth(year, month, 1, 2);
        Calendar lastSunday = dateUtil.getDayAtWeekOfMonth(year, month, lastWeek == 1 ? 4 : 5, 1);

        return weeklyReport.weeklyDate.between(LocalDateTime.of(firstMonday.get(Calendar.YEAR), firstMonday.get(Calendar.MONTH) + 1, firstMonday.get(Calendar.DATE), 0, 0),
                LocalDateTime.of(lastSunday.get(Calendar.YEAR), lastSunday.get(Calendar.MONTH), lastSunday.get(Calendar.DATE), 23, 59));
    }
}
