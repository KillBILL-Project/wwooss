package com.bigbro.wwooss.v1.service.userActiveLog;

import com.bigbro.wwooss.v1.dto.response.userActiveLog.UserActiveLogResponse;
import com.bigbro.wwooss.v1.enumType.ActiveLogType;

public interface UserActiveLogService {

    UserActiveLogResponse recordActiveLog(ActiveLogType activeLogType, Long userId);

    void verifiedLog(Long logId);
}
