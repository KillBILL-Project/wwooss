package com.bigbro.wwooss.v1.dto.response.notification;

import com.bigbro.wwooss.v1.entity.notification.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class NotificationResponse {

    Long notificationId;

    String title;

    String message;

    String deepLink;

    LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .deepLink(notification.getDeepLink())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
