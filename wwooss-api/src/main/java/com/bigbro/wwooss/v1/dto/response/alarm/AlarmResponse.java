package com.bigbro.wwooss.v1.dto.response.alarm;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlarmResponse {

    private Long alarmId;

    private List<Integer> dayOfWeekList; // 월[1] ... 일[7]

    private int sendHour;

    private int sendMinute;

    private boolean on;

    public static AlarmResponse from(Alarm alarm) {
        return AlarmResponse.builder()
                .alarmId(alarm.getAlarmId())
                .dayOfWeekList(alarm.getDayOfWeekList())
                .sendHour(alarm.getSendHour())
                .sendMinute(alarm.getSendMinute())
                .on(alarm.getIsOn())
                .build();
    }
}
