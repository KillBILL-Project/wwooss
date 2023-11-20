package com.bigbro.wwooss.v1.service.notification;

import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.List;
import java.util.Map;

public interface FirebaseService {

    /**
     *
     * @param notificationTemplate 알림 템플릿
     * @param variableMap 변수값 nullable
     * @param fcmToken 유저의 fcmToken
     */
    void sendOne(NotificationTemplate notificationTemplate, Map<String, String> variableMap,
            String fcmToken) throws FirebaseMessagingException;

    /**
     *
     * @param notificationTemplate 알림 템플릿
     * @param variableMap 변수값 nullable
     * @param fcmTokenList 발송하고자 하는 유저의 fcmToken 리스트
     */
    void sendMany(NotificationTemplate notificationTemplate, Map<String, String> variableMap, List<String> fcmTokenList)
            throws FirebaseMessagingException;
}
