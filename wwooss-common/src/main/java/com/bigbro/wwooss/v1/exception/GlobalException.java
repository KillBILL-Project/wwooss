package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<WwoossResponse<T>> serverExceptionHandler(final Exception e, final HttpServletRequest request) {
        log.error("serverExceptionHandler: {}\n url: {}", e.getMessage(), request.getRequestURL());
        log.error(e.getMessage(), e);

        return WwoossResponseUtil.wwoossNoDataResponseEntity(INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = CustomGlobalException.class)
    public ResponseEntity<WwoossResponse<Object>> customExceptionHandler(final CustomGlobalException e, final HttpServletRequest request) {
        log.error("CustomGlobalException: {} / url: {}", e.getMessage(), request.getRequestURL());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(e.getCode(), e.getTitle(), e.getMessage());
    }

}
