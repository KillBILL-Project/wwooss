package com.bigbro.wwooss.v1.dto.request.alarm;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmOnOffRequest {

    @NotNull(message = "on/off 값은 필수 입니다.")
    private boolean isOn;
}
