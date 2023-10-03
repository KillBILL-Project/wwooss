package com.bigbro.killbill.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class KillBillResponse<T> {

    private HttpStatus code;

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

    public static <T> KillBillResponse<T> of(HttpStatus code, String title, String message, T data) {
        return KillBillResponse.<T>builder()
                .code(code)
                .title(title)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> KillBillResponse<T> of(HttpStatus code, String title, String message) {
        return KillBillResponse.<T>builder()
                .code(code)
                .title(title)
                .message(message)
                .build();
    }

    public static <T> KillBillResponse<T> of(KillBillResponseCode killBillResponseCode, T data) {
        return KillBillResponse.<T>builder()
                .code(killBillResponseCode.getCode())
                .title(killBillResponseCode.getTitle())
                .message(killBillResponseCode.getMessage())
                .build();
    }
}
