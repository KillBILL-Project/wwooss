package com.bigbro.wwooss.v1.service.user;

import com.bigbro.wwooss.v1.dto.response.auth.UserResponse;

public interface UserService {

    /**
     *
     * @return 로그인 한 유저의 정보
     */
    UserResponse getUserInfo();

    /**
     * 푸쉬 동의 여부 번경
     *
     * @param pushConsent 동의 여부
     * @param userId 유저 ID
     */
    void updatePushConsentStatus(boolean pushConsent, long userId);

    /**
     * FCM 토큰 업데이트
     */
    void updateFcmToken(long userId, String fcmToken);

}
