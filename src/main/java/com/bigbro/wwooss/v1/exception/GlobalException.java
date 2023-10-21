package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.bigbro.wwooss.v1.common.WwoossResponseCode.DUPLICATION_VALUE;
import static com.bigbro.wwooss.v1.common.WwoossResponseCode.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<WwoossResponse<T>> serverExceptionHandler(final Exception e, final HttpServletRequest request) {
        log.error("serverExceptionHandler: {}\n url: {} \n {}", e.getMessage(), request.getRequestURL(), e.getStackTrace());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public <T> ResponseEntity<WwoossResponse<T>> sqlIntegrityConstraintExceptionHandler(final DataIntegrityViolationException e, final HttpServletRequest request) {
        log.error("DataIntegrityViolationException: {} / url: {}", e.getMessage(), request.getRequestURL());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(DUPLICATION_VALUE);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<WwoossResponse<Object>> DataNotFoundExceptionHandler(final DataNotFoundException e, final HttpServletRequest request) {
        log.error("DataNotFoundException: {} / url: {}", e.getMessage(), request.getRequestURL());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(e.getCode(), e.getTitle(), e.getMessage());
    }

}
