package com.bigbro.wwooss.v1.repository.alarm.custom;

import static com.bigbro.wwooss.v1.entity.alarm.QAlarm.alarm;
import static com.bigbro.wwooss.v1.entity.user.QUser.user;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Alarm> findAlarmAtTime(int hour, int minute, int day) {
        return queryFactory.selectFrom(alarm)
                .join(alarm.user, user)
                .where(alarm.sendHour.eq(hour)
                        .and(alarm.sendMinute.eq(minute))
                        .and(alarm.isOn.eq(true))
                        .and(user.pushConsent.eq(true)))
                .fetch();
    }
}
