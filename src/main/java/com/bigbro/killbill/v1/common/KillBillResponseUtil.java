package com.bigbro.killbill.v1.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.bigbro.killbill.v1.common.KillBillResponseCode.CREATED_SUCCESS;
import static com.bigbro.killbill.v1.common.KillBillResponseCode.SUCCESS;

public class KillBillResponseUtil {

    public static <T> ResponseEntity <KillBillResponse<T>> responseOkNoData() {
        return killbillNoDataResponseEntity(SUCCESS);
    }

    public static <T> ResponseEntity <KillBillResponse<T>> responseOkAddData(T data) {
        return killbillResponseEntity(SUCCESS, data);
    }

    public static <T> ResponseEntity <KillBillResponse<T>> responseCreatedNoData() {
        return killbillNoDataResponseEntity(CREATED_SUCCESS);
    }

    public static <T> ResponseEntity <KillBillResponse<T>> responseCreatedAddData(T data) {
        return killbillResponseEntity(CREATED_SUCCESS, data);
    }


    public static <T> ResponseEntity<KillBillResponse<T>> killbillNoDataResponseEntity(HttpStatus status, String title, String message) {
        return ResponseEntity
                .status(status)
                .body(KillBillResponse.of(status, title, message));
    }

    public static <T> ResponseEntity<KillBillResponse<T>> killbillNoDataResponseEntity(KillBillResponseCode killBillResponseCode) {
        return ResponseEntity
                .status(killBillResponseCode.getCode())
                .body(KillBillResponse.from(killBillResponseCode));
    }

    public static <T> ResponseEntity<KillBillResponse<T>> killbillResponseEntity(HttpStatus status, String title, String message, T data) {
        return ResponseEntity
                .status(status)
                .body(KillBillResponse.of(status, title, message, data));
    }

    public static <T> ResponseEntity<KillBillResponse<T>> killbillResponseEntity(KillBillResponseCode killBillResponseCode, T data) {
        return ResponseEntity
                .status(killBillResponseCode.getCode())
                .body(KillBillResponse.of(killBillResponseCode, data));
    }

}


