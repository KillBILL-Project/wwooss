package com.bigbro.wwooss.v1.service.notification;

import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.Map;

public interface FirebaseService {

    /**
     *
     * @param notificationTemplate 알림 템플릿
     * @param variableMap 변수값 nullable
     * @param fcmToken fcmToken
     */
    void sendOne(NotificationTemplate notificationTemplate,
            Map<String, String> variableMap,
            String fcmToken) throws FirebaseMessagingException;

    void sendMany();
}
