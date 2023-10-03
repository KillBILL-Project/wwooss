package com.bigbro.killbill.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class KillBillResponse<T> {

    private String code;

    private String title;

    private String message;

    private T data;

    public static <T> KillBillResponse<T> from(KillBillResponseCode responseCode) {
        return KillBillResponse.<T>builder()
                .code(responseCode.getCode())
                .title(responseCode.getTitle())
                .message(responseCode.getMessage())
                .build();
    }

    public static <T> KillBillResponse<T> of(KillBillResponseCode responseCode, T data) {
        return KillBillResponse.<T>builder()
                .code(responseCode.getCode())
                .title(responseCode.getTitle())
                .message(responseCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> KillBillResponse<T> of(String code, String title, String message) {
        return KillBillResponse.<T>builder()
                .code(code)
                .title(title)
                .message(message)
                .build();
    }
}
