package com.bigbro.wwooss.v1.domain.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GetUserResponse {
    private Long userId;
    private String userName;

    public static GetUserResponse of(Long userId, String userName) {
        return GetUserResponse.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }
}
