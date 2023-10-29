package com.bigbro.wwooss.v1.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.CREATED_SUCCESS;
import static com.bigbro.wwooss.v1.response.WwoossResponseCode.SUCCESS;

public class WwoossResponseUtil {

    public static <T> ResponseEntity <WwoossResponse<T>> responseOkNoData() {
        return wwoossNoDataResponseEntity(SUCCESS);
    }

    public static <T> ResponseEntity <WwoossResponse<T>> responseOkAddData(T data) {
        return wwoossResponseEntity(SUCCESS, data);
    }

    public static <T> ResponseEntity <WwoossResponse<T>> responseCreatedNoData() {
        return wwoossNoDataResponseEntity(CREATED_SUCCESS);
    }

    public static <T> ResponseEntity <WwoossResponse<T>> responseCreatedAddData(T data) {
        return wwoossResponseEntity(CREATED_SUCCESS, data);
    }


    public static <T> ResponseEntity<WwoossResponse<T>> wwoossNoDataResponseEntity(HttpStatus status, String title, String message) {
        return ResponseEntity
                .status(status)
                .body(WwoossResponse.of(status, title, message));
    }

    public static <T> ResponseEntity<WwoossResponse<T>> wwoossNoDataResponseEntity(WwoossResponseCode wwoossResponseCode) {
        return ResponseEntity
                .status(wwoossResponseCode.getCode())
                .body(WwoossResponse.from(wwoossResponseCode));
    }

    public static <T> ResponseEntity<WwoossResponse<T>> wwoossResponseEntity(HttpStatus status, String title, String message, T data) {
        return ResponseEntity
                .status(status)
                .body(WwoossResponse.of(status, title, message, data));
    }

    public static <T> ResponseEntity<WwoossResponse<T>> wwoossResponseEntity(WwoossResponseCode wwoossResponseCode, T data) {
        return ResponseEntity
                .status(wwoossResponseCode.getCode())
                .body(WwoossResponse.of(wwoossResponseCode, data));
    }

}


