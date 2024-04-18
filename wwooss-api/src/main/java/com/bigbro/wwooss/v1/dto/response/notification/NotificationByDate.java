package com.bigbro.wwooss.v1.dto.response.notification;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NotificationByDate {
    String date;
    List<NotificationResponse> notificationResponses;

    public static NotificationByDate of(String date, List<NotificationResponse> notificationResponses) {
        return NotificationByDate.builder()
                .date(date)
                .notificationResponses(notificationResponses)
                .build();
    }
}
