package com.bigbro.wwooss.v1.repository.alarm.custom;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import java.util.List;

public interface AlarmRepositoryCustom {

    List<Alarm> findAlarmAtTime(int hour, int minute, int day);

}
