package com.bigbro.wwooss.v1.service.template;

import java.util.concurrent.TimeUnit;

public interface RedisTemplateService {
    /**
     * 레디스 값 가져오기
     *
     * @param keyName 키 이름
     * @return
     */
    String getRedisByKey(String keyName);

    /**
     * 레디스 값 설정
     *
     * @param keyName      키 명
     * @param value        값
     * @param expiredValue 만료 시간
     * @param timeUnit     만료 시간 단위
     */
    void setRedisValue(final String keyName, final String value, final Long expiredValue, final TimeUnit timeUnit);

    /**
     * Redis 키 존재여부 확인
     *
     * @param keyName 키 명
     * @return True:존재 / False:미존재
     */
    Boolean isExistKey(final String keyName);
}
