package com.bigbro.wwooss.v1.service.alarm;

import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;

public interface AlarmService {

    /**
     * 알람 생성
     * @parma userId 유저정보
     * @param alarmRequest 알람 정보 요청 (요일, 시간, 분)
     *
     * @return alarmResponse 알람 정보 응답 (요일, 시간, 분, on/off)
     */
    AlarmResponse createAlarm(Long userId, AlarmRequest alarmRequest);
}
