package com.bigbro.wwooss.v1.service.alarm.impl;

import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;
import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.exception.DataNotFoundException;
import com.bigbro.wwooss.v1.repository.alarm.AlarmRepository;
import com.bigbro.wwooss.v1.repository.user.UserRepository;
import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import com.bigbro.wwooss.v1.service.alarm.AlarmService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;

    private final UserRepository userRepository;

    @Transactional
    @Override
    public AlarmResponse createAlarm(Long userId, AlarmRequest alarmRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        Alarm alarm = Alarm.of(alarmRequest.getDayOfWeekList(),
                alarmRequest.getSendHour(),
                alarmRequest.getSendMinute(),
                user);

        return AlarmResponse.from(alarmRepository.save(alarm));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlarmResponse> getAlarmList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 유저입니다."));
        List<Alarm> alarmList = alarmRepository.findAlarmByUser(user);

        return alarmList.stream().map(AlarmResponse::from).toList();
    }

    @Override
    public AlarmResponse updateAlarm(Long alarmId, AlarmRequest alarmRequest) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new DataNotFoundException(WwoossResponseCode.NOT_FOUND_DATA, "존재하지 않는 알람입니다."));

        alarm.updateAlarm(alarmRequest.getDayOfWeekList(), alarmRequest.getSendHour(), alarmRequest.getSendMinute());
        return AlarmResponse.from(alarm);
    }
}
