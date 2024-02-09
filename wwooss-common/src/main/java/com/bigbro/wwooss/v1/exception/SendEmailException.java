package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class SendEmailException extends CustomGlobalException {

    public SendEmailException(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public SendEmailException(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
