package com.bigbro.wwooss.v1.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class WwoossResponse<T> {

    private int code;

    private String title;

    private String message;

    private T data;

    public static <T> WwoossResponse<T> from(WwoossResponseCode responseCode) {
        return WwoossResponse.<T>builder()
                .code(responseCode.getCode().value())
                .title(responseCode.getTitle())
                .message(responseCode.getMessage())
                .build();
    }

    public static <T> WwoossResponse<T> of(HttpStatus code, String title, String message, T data) {
        return WwoossResponse.<T>builder()
                .code(code.value())
                .title(title)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> WwoossResponse<T> of(HttpStatus code, String title, String message) {
        return WwoossResponse.<T>builder()
                .code(code.value())
                .title(title)
                .message(message)
                .build();
    }

    public static <T> WwoossResponse<T> of(WwoossResponseCode WwoossResponseCode, T data) {
        return WwoossResponse.<T>builder()
                .code(WwoossResponseCode.getCode().value())
                .title(WwoossResponseCode.getTitle())
                .message(WwoossResponseCode.getMessage())
                .data(data)
                .build();
    }
}
