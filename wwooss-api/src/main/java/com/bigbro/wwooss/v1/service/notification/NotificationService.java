package com.bigbro.wwooss.v1.service.notification;

import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;

public interface NotificationService {

    /**
     * 단일 발송
     * @param notificationSendRequest 단건 발송 요청 정보
     * 단건 발송이기 때문에 여러 target을 담아 보내도 첫번 째 target만 발송
     */
    void sendOne(NotificationSendRequest notificationSendRequest);

    /**
     * 일괄 발송
     * @param notificationSendRequest 일괄 발송 요청 정보
     * 모든 target에게 발송
     */
    void sendMany(NotificationSendRequest notificationSendRequest);

}
