package com.bigbro.killbill.v1.common;

public class KillBillResponseUtil {

    public static <T> KillBillResponse<T> responseOkNoData() {
        return KillBillResponse.from(KillBillResponseCode.SUCCESS);
    }

    public static <T> KillBillResponse<T> responseOkAddData(T data) {
        return KillBillResponse.of(KillBillResponseCode.SUCCESS, data);
    }

    public static <T> KillBillResponse<T> responseCustomMessageNoData(KillBillResponseCode responseCode) {
        return KillBillResponse.from(responseCode);
    }

    public static <T> KillBillResponse<T> responseCustomMessageNoData(String code, String title, String message) {
        return KillBillResponse.of(code, title, message);
    }

    public static <T> KillBillResponse<T> responseOkAddDataCustomMessage(T data, KillBillResponseCode responseCode) {
        return KillBillResponse.of(responseCode, data);
    }
}


