package com.bigbro.wwooss.v1.dto.request.auth;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordRequest {
    @NotNull(message = "변경할 비밀번호는 필수입니다.")
    private String password;
}
