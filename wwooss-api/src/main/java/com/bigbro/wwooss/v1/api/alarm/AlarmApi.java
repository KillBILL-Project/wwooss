package com.bigbro.wwooss.v1.api.alarm;

import com.bigbro.wwooss.v1.dto.request.alarm.AlarmOnOffRequest;
import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.alarm.AlarmService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/alarm")
@RequiredArgsConstructor
public class AlarmApi {

    private final AlarmService alarmService;

    @PostMapping
    public ResponseEntity<WwoossResponse<AlarmResponse>> createAlarm(@RequestBody @Valid AlarmRequest alarmRequest,
            UserCredential userCredential) {
        return WwoossResponseUtil.responseCreatedAddData(alarmService.createAlarm(userCredential.getUserId(), alarmRequest));
    }

    @GetMapping
    public ResponseEntity<WwoossResponse<List<AlarmResponse>>> getAlarmList(UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(alarmService.getAlarmList(userCredential.getUserId()));
    }

    @PatchMapping("/{alarm-id}")
    public ResponseEntity<WwoossResponse<AlarmResponse>> updateAlarm(@RequestBody @Valid AlarmRequest alarmRequest,
            @PathVariable("alarm-id") Long aramId) {
        return WwoossResponseUtil.responseOkAddData(alarmService.updateAlarm(aramId, alarmRequest));
    }

    @PatchMapping("/{alarm-id}/on-off")
    public ResponseEntity<WwoossResponse<Void>> switchAlarm(@RequestBody @Valid AlarmOnOffRequest alarmOnOffRequest,
            @PathVariable("alarm-id") Long aramId) {
        alarmService.switchAlarm(aramId, alarmOnOffRequest);

        return WwoossResponseUtil.responseOkNoData();
    }

    @DeleteMapping("/{alarm-id}")
    public ResponseEntity<WwoossResponse<Void>> deleteAlarm(@PathVariable("alarm-id") Long alarmId) {
        alarmService.deleteAlarm(alarmId);
        return WwoossResponseUtil.responseOkNoData();
    }
}
