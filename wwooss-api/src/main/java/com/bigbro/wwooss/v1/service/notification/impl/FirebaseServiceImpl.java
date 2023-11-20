package com.bigbro.wwooss.v1.service.notification.impl;

import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.bigbro.wwooss.v1.service.notification.FirebaseService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseServiceImpl implements FirebaseService {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendOne(final NotificationTemplate notificationTemplate, final Map<String, String> variableMap,
            String fcmToken) throws FirebaseMessagingException {

        Notification notification = buildNotification(notificationTemplate, variableMap);
        firebaseMessaging.send(messageBuild(notification, fcmToken));
    }

    @Override
    public void sendMany() {

    }

    private Message messageBuild(Notification notification, String fcmToken) {
        return Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();
    }

    /**
     *
     * @param notificationTemplate 푸쉬 알림 template 정보
     * @return Notification
     */
    private Notification buildNotification(final NotificationTemplate notificationTemplate, final Map<String, String> variableMap) {
        return Notification.builder()
                .setTitle(notificationTemplate.getTitle())
                .setBody(getNotificationBody(notificationTemplate, variableMap))
                .build();
    }

    /**
     * 푸쉬 알림 template에 있는 변수 치환 작업
     * 예) 안녕하세요. {name}님 ( template 내용 )
     * -> 안녕하세요. 홍길동님
     */
    public String getNotificationBody(final NotificationTemplate notificationTemplate, final Map<String, String> variableMap) {
        String content = Optional.ofNullable(notificationTemplate.getMessage()).orElse(StringUtils.EMPTY);

        if (Objects.nonNull(variableMap)) {
            final Set<String> variableNames = variableMap.keySet();
            for (String variableName : variableNames) {
                content = content.replace(variableName, variableMap.get(variableName));
            }
        }
        return content;
    }
}
