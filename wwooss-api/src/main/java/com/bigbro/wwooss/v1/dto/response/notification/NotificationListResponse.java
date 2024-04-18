package com.bigbro.wwooss.v1.dto.response.notification;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NotificationListResponse {

    Boolean hasNext;

    List<NotificationByDate> notificationByDates;

    public static NotificationListResponse of(Boolean hasNext, List<NotificationByDate> notificationByDates) {
       return NotificationListResponse.builder()
               .hasNext(hasNext)
               .notificationByDates(notificationByDates)
               .build();
    }
}
