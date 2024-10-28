package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class DataTooManyException extends CustomGlobalException {

    public DataTooManyException(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public DataTooManyException(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
