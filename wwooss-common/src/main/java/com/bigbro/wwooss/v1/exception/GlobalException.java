package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.INTERNAL_SERVER_ERROR;
import static com.bigbro.wwooss.v1.response.WwoossResponseCode.NO_DATA;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<WwoossResponse<T>> serverExceptionHandler(final Exception e, final HttpServletRequest request) {
        log.error("serverExceptionHandler: {}\n url: {}", e.getMessage(), request.getRequestURL());
        log.error(e.getMessage(), e);

        return WwoossResponseUtil.wwoossNoDataResponseEntity(INTERNAL_SERVER_ERROR.getCode(), "에러", INTERNAL_SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(value = CustomGlobalException.class)
    public ResponseEntity<WwoossResponse<Object>> customExceptionHandler(final CustomGlobalException e, final HttpServletRequest request) {
        log.error("CustomGlobalException: {} / url: {}", e.getMessage(), request.getRequestURL());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(e.getCode(), e.getTitle(), e.getMessage());
    }

    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public ResponseEntity<WwoossResponse<Object>> missingExceptionHandler(final MissingServletRequestPartException e, final HttpServletRequest request) {
        log.error("MissingServletRequestPartException: {} / url: {}", e.getMessage(), request.getRequestURL());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(NO_DATA.getCode(), "NO File", e.getMessage());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<WwoossResponse<Object>> missingExceptionHandler(final MissingServletRequestParameterException e, final HttpServletRequest request) {
        log.error("MissingServletRequestParameterException: {} / url: {}", e.getMessage(), request.getRequestURL());

        return WwoossResponseUtil.wwoossNoDataResponseEntity(NO_DATA.getCode(), "No Parameter", e.getMessage());
    }

}
