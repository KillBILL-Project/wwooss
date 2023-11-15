package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomGlobalException extends RuntimeException {
    private final HttpStatus code;
    private final String title;
    private final String message;
    private final String customServerLog;

    public CustomGlobalException(WwoossResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = responseCode.getMessage();
        this.customServerLog = "";
    }

    public CustomGlobalException(WwoossResponseCode responseCode, String message) {
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = message;
        this.customServerLog = "";
    }
}
