package com.bigbro.wwooss.v1.service.user;

public interface LoginLogService {
    /**
     * 로그인 시 쌓이는 데이터
     * 1일 1회만 생성
     */
    void createLoginLog(Long userId);
}
