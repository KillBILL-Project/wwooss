package com.bigbro.wwooss.v1.service.notification;

public interface NotificationService {

    /**
     * 단일 발송
     */

    void sendOne();

    /**
     * 일괄 발송
     */
    void sendMany();

}
