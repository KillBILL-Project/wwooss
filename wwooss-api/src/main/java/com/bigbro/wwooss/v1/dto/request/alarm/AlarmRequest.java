package com.bigbro.wwooss.v1.dto.request.alarm;

import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AlarmRequest {

    @NotEmpty(message = "요일 선택을 필수입니다.")
    private List<DayOfWeek> dayOfWeekList;

    @NotNull(message = "알람 발송 시간(시) 필수값입니다.")
    private int sendHour;

    @NotNull(message = "알람 발송 시간(분) 필수값입니다.")
    private int sendMinute;

}
