package com.bigbro.wwooss.v1.api.alarm;

import com.bigbro.wwooss.v1.dto.request.alarm.AlarmRequest;
import com.bigbro.wwooss.v1.dto.response.alarm.AlarmResponse;
import com.bigbro.wwooss.v1.dto.response.auth.UserResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.alarm.AlarmService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/alarm")
@RequiredArgsConstructor
public class AlarmApi {

    private final AlarmService alarmService;

    @PostMapping
    public ResponseEntity<WwoossResponse<AlarmResponse>> getUserInfo(@RequestBody @Valid AlarmRequest alarmRequest) {
        // TODO: userId 넣기
        return WwoossResponseUtil.responseOkAddData(alarmService.createAlarm(1L, alarmRequest));
    }

}
