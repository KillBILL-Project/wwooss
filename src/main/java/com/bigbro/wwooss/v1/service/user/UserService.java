package com.bigbro.wwooss.v1.service.user;

public interface UserService {

    /**
     * 푸쉬 동의 여부 번경
     *
     * @param pushConsent 동의 여부
     * @param userId 유저 ID
     */
    public void updatePushConsentStatus(boolean pushConsent, long userId);
}
