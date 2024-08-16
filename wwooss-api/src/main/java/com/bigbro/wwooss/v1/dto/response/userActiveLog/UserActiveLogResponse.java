package com.bigbro.wwooss.v1.dto.response.userActiveLog;

import com.bigbro.wwooss.v1.entity.userActiveLog.UserActiveLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserActiveLogResponse {
    private Long userActiveLogId;
    private String contents;
    private Double value;

    public static UserActiveLogResponse from(UserActiveLog userActiveLog) {
        return UserActiveLogResponse.builder()
                .userActiveLogId(userActiveLog.getUserActiveLogId())
                .contents(userActiveLog.getContents())
                .value(userActiveLog.getValue())
                .build();
    }
}
