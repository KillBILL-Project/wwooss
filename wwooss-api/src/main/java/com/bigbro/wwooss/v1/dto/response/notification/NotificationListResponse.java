package com.bigbro.wwooss.v1.dto.response.notification;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class NotificationListResponse {

    Boolean hasNext;

    List<NotificationResponse> notificationResponses;

    public static NotificationListResponse of(Boolean hasNext, List<NotificationResponse> notificationResponses) {
       return NotificationListResponse.builder()
               .hasNext(hasNext)
               .notificationResponses(notificationResponses)
               .build();
    }
}
