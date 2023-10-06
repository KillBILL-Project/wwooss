package com.bigbro.wwooss.v1.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class WwoossResponse<T> {

    private HttpStatus code;

    private String title;

    private String message;

    private T data;

    public static <T> WwoossResponse<T> from(WwoossResponseCode responseCode) {
        return WwoossResponse.<T>builder()
                .code(responseCode.getCode())
                .title(responseCode.getTitle())
                .message(responseCode.getMessage())
                .build();
    }

    public static <T> WwoossResponse<T> of(HttpStatus code, String title, String message, T data) {
        return WwoossResponse.<T>builder()
                .code(code)
                .title(title)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> WwoossResponse<T> of(HttpStatus code, String title, String message) {
        return WwoossResponse.<T>builder()
                .code(code)
                .title(title)
                .message(message)
                .build();
    }

    public static <T> WwoossResponse<T> of(WwoossResponseCode WwoossResponseCode, T data) {
        return WwoossResponse.<T>builder()
                .code(WwoossResponseCode.getCode())
                .title(WwoossResponseCode.getTitle())
                .message(WwoossResponseCode.getMessage())
                .build();
    }
}
