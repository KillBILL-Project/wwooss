package com.bigbro.wwooss.v1.service.template.impl;

import com.bigbro.wwooss.v1.service.template.RedisTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTemplateServiceImpl implements RedisTemplateService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getRedisByKey(String keyName) {
        return this.redisTemplate.opsForValue().get(keyName);
    }

    @Override
    public void setRedisValue(final String keyName, final String value, final Long expiredValue, final TimeUnit timeUnit) {
        this.redisTemplate.opsForValue().set(keyName, value, expiredValue, timeUnit);
    }

    @Override
    public Boolean isExistKey(final String keyName) {
        return this.redisTemplate.hasKey(keyName);
    }
}
