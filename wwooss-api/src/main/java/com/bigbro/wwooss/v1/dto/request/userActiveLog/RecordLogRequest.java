package com.bigbro.wwooss.v1.dto.request.userActiveLog;

import com.bigbro.wwooss.v1.enumType.ActiveLogType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordLogRequest {
    private ActiveLogType activeLogType;
}
