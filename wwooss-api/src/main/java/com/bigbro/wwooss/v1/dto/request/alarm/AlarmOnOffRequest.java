package com.bigbro.wwooss.v1.dto.request.alarm;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlarmOnOffRequest {

    @NotNull(message = "on/off 값은 필수 입니다.")
    private boolean isOn;
}
