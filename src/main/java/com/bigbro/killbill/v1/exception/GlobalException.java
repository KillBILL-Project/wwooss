package com.bigbro.killbill.v1.exception;

import com.bigbro.killbill.v1.common.KillBillResponse;
import com.bigbro.killbill.v1.common.KillBillResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.bigbro.killbill.v1.common.KillBillResponseCode.DUPLICATION_VALUE;
import static com.bigbro.killbill.v1.common.KillBillResponseCode.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<KillBillResponse<T>> serverExceptionHandler(final Exception e, final HttpServletRequest request) {
        log.error("serverExceptionHandler: {} {}", e.getMessage(), request.getRequestURL());

        return KillBillResponseUtil.killbillNoDataResponseEntity(INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public <T> ResponseEntity<KillBillResponse<T>> sqlIntegrityConstraintExceptionHandler(final DataIntegrityViolationException e, final HttpServletRequest request) {
        log.error("DataIntegrityViolationException: {} {}", e.getMessage(), request.getRequestURL());

        return KillBillResponseUtil.killbillNoDataResponseEntity(DUPLICATION_VALUE);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<KillBillResponse<Object>> DataNotFoundExceptionHandler(final DataNotFoundException e, final HttpServletRequest request) {
        log.error("DataNotFoundException: {} {}", e.getMessage(), request.getRequestURL());

        return KillBillResponseUtil.killbillNoDataResponseEntity(e.getCode(), e.getTitle(), e.getMessage());
    }

}