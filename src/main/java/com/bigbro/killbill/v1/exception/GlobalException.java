package com.bigbro.killbill.v1.exception;

import com.bigbro.killbill.v1.common.KillBillResponse;
import com.bigbro.killbill.v1.common.KillBillResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.bigbro.killbill.v1.common.KillBillResponseCode.DUPLICATION_VALUE;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> serverExceptionHandler(Exception ex) {
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public <T> ResponseEntity<KillBillResponse<T>> sqlIntegrityConstraintExceptionHandler(DataIntegrityViolationException e) {
        return ResponseEntity.ok(KillBillResponseUtil.responseCustomMessageNoData(DUPLICATION_VALUE));
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public <T> ResponseEntity<KillBillResponse<T>> DataNotFoundExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.ok(KillBillResponseUtil.responseCustomMessageNoData(e.getCode(), e.getTitle(), e.getMessage()));
    }

}