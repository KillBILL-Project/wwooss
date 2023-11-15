package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class IncorrectDataException extends CustomGlobalException {

    public IncorrectDataException(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public IncorrectDataException(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
