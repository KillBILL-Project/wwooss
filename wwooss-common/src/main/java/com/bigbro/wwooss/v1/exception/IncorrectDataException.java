package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IncorrectDataException extends RuntimeException{

    private final HttpStatus code;
    private final String title;
    private final String message;
    private final String customServerLog;

    public IncorrectDataException(WwoossResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = responseCode.getMessage();
        this.customServerLog = "";
    }

    public IncorrectDataException(WwoossResponseCode responseCode, String message) {
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = message;
        this.customServerLog = "";
    }
}
