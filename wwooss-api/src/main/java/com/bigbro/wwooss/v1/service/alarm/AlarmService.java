package com.bigbro.wwooss.v1.service.alarm;

import com.bigbro.wwooss.v1.dto.request.alarm.AlarmOnOffRequest;
import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;
import java.util.List;

public interface AlarmService {

    /**
     * 알람 생성
     * @parma userId 유저정보
     * @param alarmRequest 알람 정보 요청 (요일, 시간, 분)
     *
     * @return alarmResponse 알람 정보 응답 (요일, 시간, 분, on/off)
     */
    AlarmResponse createAlarm(Long userId, AlarmRequest alarmRequest);

    /**
     * 알림 목록
     * @param userId 유저 ID
     *
     * @return alarmResponseList 알람 정보 응답 (요일, 시간, 분, on/off)
     */
    List<AlarmResponse> getAlarmList(Long userId);

    /**
     * 알람 생성
     * @parma alarmId 알람 ID
     * @param alarmRequest 알람 정보 요청 (요일, 시간, 분)
     *
     * @return alarmResponse 알람 정보 응답 (요일, 시간, 분, on/off)
     */
    AlarmResponse updateAlarm(Long alarmId, AlarmRequest alarmRequest);

    /**
     * 알람 on/off
     * @parma alarmId 알람 ID
     * @param alarmOnOffRequest 알람 정보 요청 (요일, 시간, 분)
     *
     */
    void switchAlarm(Long alarmId, AlarmOnOffRequest alarmOnOffRequest);

    /**
     * 알람 삭제
     * @parma alarmId 알람 ID
     *
     */
    void deleteAlarm(Long alarmId);
}
