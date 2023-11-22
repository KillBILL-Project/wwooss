package com.bigbro.wwooss.v1.service.notification.impl;

import com.bigbro.wwooss.v1.entity.notification.NotificationTemplate;
import com.bigbro.wwooss.v1.service.notification.FirebaseService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
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
    public void sendMany(final NotificationTemplate notificationTemplate, final Map<String, String> variableMap,
            final List<String> fcmTokenList) throws FirebaseMessagingException {

        Notification notification = buildNotification(notificationTemplate, variableMap);
        sendMulticast(notification, fcmTokenList);
    }

    private Message messageBuild(final Notification notification, final String fcmToken) {
        return Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();
    }

    private MulticastMessage multicastMessageBuild(final Notification notification, final List<String> fcmTokenList,
            int fromIndex, int toIndex) {
        return MulticastMessage.builder()
                .addAllTokens(fcmTokenList.subList(fromIndex, toIndex))
                .setNotification(notification)
                .build();
    }

    /**
     * 푸쉬 알림 최대 보낼 수 있는 건수 : 500건
     * 500건씩 나눠서 발송
     */
    private void sendMulticast(final Notification notification, final List<String> fcmTokens) throws FirebaseMessagingException {
        int maxSend = 500;
        int loopCount = (int) Math.ceil((double) fcmTokens.size() / maxSend);

        for (int i = 0; i < loopCount; i++) {
            int fromIndex = maxSend * i;
            int toIndex = Math.min(maxSend * (i + 1) - 1, fcmTokens.size());

            firebaseMessaging.sendEachForMulticast(
                    multicastMessageBuild(notification, fcmTokens, fromIndex, toIndex));

        }
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
