package com.bigbro.wwooss.v1.dto.request.auth;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResetPasswordRequest {
    private String email;

    public static ResetPasswordRequest from(String email) {
        return ResetPasswordRequest.builder()
                .email(email)
                .build();
    }
}
