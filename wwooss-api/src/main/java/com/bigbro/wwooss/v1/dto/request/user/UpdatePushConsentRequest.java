package com.bigbro.wwooss.v1.dto.request.user;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePushConsentRequest {

    @NotNull(message = "푸쉬 동의 여부에 대한 값은 필수입니다.")
    private boolean pushConsent;

}
