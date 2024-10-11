package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class NoDataException extends CustomGlobalException {
    public NoDataException(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public NoDataException(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
