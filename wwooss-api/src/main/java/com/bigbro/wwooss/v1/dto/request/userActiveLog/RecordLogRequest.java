package com.bigbro.wwooss.v1.dto.request.userActiveLog;

import com.bigbro.wwooss.v1.enumType.ActiveLogType;
import lombok.Getter;

@Getter
public class RecordLogRequest {
    private ActiveLogType activeLogType;
}
