package com.bigbro.wwooss.v1.dto.request.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResetPasswordRequest {
    private String email;

    public static ResetPasswordRequest from(String email) {
        return ResetPasswordRequest.builder()
                .email(email)
                .build();
    }
}
