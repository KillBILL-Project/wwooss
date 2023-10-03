package com.bigbro.killbill.v1.exception;

import com.bigbro.killbill.v1.common.KillBillResponseCode;
import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {

    private final KillBillResponseCode responseCode;
    private final String code;
    private final String title;
    private final String message;
    private final String customServerLog;

    public DataNotFoundException(KillBillResponseCode responseCode) {
        this.responseCode = responseCode;
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = responseCode.getMessage();
        this.customServerLog = "";
    }

    public DataNotFoundException(KillBillResponseCode responseCode, String message) {
        this.responseCode = responseCode;
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = message;
        this.customServerLog = "";
    }

    public DataNotFoundException(KillBillResponseCode responseCode, String message, String customServerLog) {
        this.responseCode = responseCode;
        this.code = responseCode.getCode();
        this.title = responseCode.getTitle();
        this.message = message;
        this.customServerLog = customServerLog;
    }
}