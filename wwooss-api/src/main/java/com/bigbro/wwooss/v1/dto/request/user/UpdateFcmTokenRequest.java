package com.bigbro.wwooss.v1.dto.request.user;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateFcmTokenRequest {

    @NotNull(message = "FCM Token은 필수입니다.")
    private String fcmToken;

}
