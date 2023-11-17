package com.bigbro.wwooss.v1.repository.alarm.custom;

import static com.bigbro.wwooss.v1.entity.alarm.QAlarm.alarm;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Alarm> findAlarmAtTime(int hour, int minute, int day) {
        return queryFactory.selectFrom(alarm)
                .where(alarm.sendHour.eq(hour)
                        .and(alarm.sendMinute.eq(minute))
                        .and(alarm.dayOfWeekList.contains(day))
                        .and(alarm.isOn.eq(true)))
                .fetch();
    }
}
