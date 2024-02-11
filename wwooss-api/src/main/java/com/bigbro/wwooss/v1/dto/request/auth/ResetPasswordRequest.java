package com.bigbro.wwooss.v1.dto.request.auth;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResetPasswordRequest {

    @NotNull(message = "이메일은 필수입니다.")
    private String email;

    public static ResetPasswordRequest from(String email) {
        return ResetPasswordRequest.builder()
                .email(email)
                .build();
    }
}
