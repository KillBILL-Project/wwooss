package com.bigbro.wwooss.v1.service.userActiveLog;

import com.bigbro.wwooss.v1.dto.response.userActiveLog.UserActiveLogResponse;
import com.bigbro.wwooss.v1.enumType.ActiveLogType;

public interface UserActiveLogService {

    /**
     * 활동 로그 벨류 총합
     * @param userId
     * @return
     */
    Double getActiveLogTotalValue(Long userId);

    /**
     * 인증 활동 로그 벨류 총합
     * @param userId
     * @return
     */
    Double getVerifiedLogTotalValue(Long userId);

    /**
     * 활동 로그 기록
     * @param activeLogType
     * @param userId
     * @return
     */
    UserActiveLogResponse recordActiveLog(ActiveLogType activeLogType, Long userId);

    /**
     * 활동 로그 인증
     * @param logId
     * @return
     */
    UserActiveLogResponse verifiedLog(Long logId);
}
