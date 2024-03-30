package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class DuplicationException extends CustomGlobalException {

    public DuplicationException(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public DuplicationException(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
