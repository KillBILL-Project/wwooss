package com.bigbro.killbill.v1.exception;

import com.bigbro.killbill.v1.common.KillBillResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataNotFoundException extends RuntimeException {

    private final HttpStatus code;
    private final String title;
    private final String message;
    private final String customServerLog;

    public DataNotFoundException(KillBillResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = responseCode.getMessage();
        this.customServerLog = "";
    }

    public DataNotFoundException(KillBillResponseCode responseCode, String message) {
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = message;
        this.customServerLog = "";
    }

}