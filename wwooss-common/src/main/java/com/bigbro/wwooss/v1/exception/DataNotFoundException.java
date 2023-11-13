package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class DataNotFoundException extends CustomGlobalException {
    public DataNotFoundException(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public DataNotFoundException(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
