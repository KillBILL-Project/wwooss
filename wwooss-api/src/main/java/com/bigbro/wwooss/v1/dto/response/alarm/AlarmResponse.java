package com.bigbro.wwooss.v1.dto.response.alarm;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlarmResponse {

    private List<DayOfWeek> dayOfWeekList;

    private int sendHour;

    private int sendMinute;

    private boolean isOn;

    public static AlarmResponse from(Alarm alarm) {
        return AlarmResponse.builder()
                .dayOfWeekList(alarm.getDayOfWeekList())
                .sendHour(alarm.getSendHour())
                .sendMinute(alarm.getSendMinute())
                .isOn(alarm.getIsOn())
                .build();
    }
}
