package com.bigbro.wwooss.v1.dto.request.alarm;

import java.util.List;
import javax.validation.constraints.NotEmpty;
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
public class AlarmRequest {

    @NotEmpty(message = "요일 선택을 필수입니다.")
    private List<Integer> dayOfWeekList;

    @NotNull(message = "알람 발송 시간(시) 필수값입니다.")
    private int sendHour;

    @NotNull(message = "알람 발송 시간(분) 필수값입니다.")
    private int sendMinute;

}
