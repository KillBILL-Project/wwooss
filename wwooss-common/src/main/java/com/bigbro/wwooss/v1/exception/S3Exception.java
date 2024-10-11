package com.bigbro.wwooss.v1.exception;

import com.bigbro.wwooss.v1.response.WwoossResponseCode;
import lombok.Getter;

@Getter
public class S3Exception extends CustomGlobalException {

    public S3Exception(WwoossResponseCode responseCode) {
        super(responseCode);
    }

    public S3Exception(WwoossResponseCode responseCode, String message) {
        super(responseCode, message);
    }
}
