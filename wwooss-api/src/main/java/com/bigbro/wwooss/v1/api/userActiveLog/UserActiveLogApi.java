package com.bigbro.wwooss.v1.api.userActiveLog;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.request.userActiveLog.RecordLogRequest;
import com.bigbro.wwooss.v1.dto.response.userActiveLog.UserActiveLogResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.userActiveLog.UserActiveLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user-active-log")
@RequiredArgsConstructor
public class UserActiveLogApi {

    private final UserActiveLogService userActiveLogService;

    @PostMapping
    public ResponseEntity<WwoossResponse<UserActiveLogResponse>> recordLog(@Valid @RequestBody RecordLogRequest recordLogRequest
            , UserCredential userCredential) {

        return WwoossResponseUtil.responseCreatedAddData(userActiveLogService.recordActiveLog(recordLogRequest.getActiveLogType(),
                userCredential.getUserId()));
    }

    @PatchMapping("/{user-active-log-id}")
    public ResponseEntity<WwoossResponse<Void>> verifiedLog(@PathVariable("user-active-log-id") Long userActiveLogId) {
        userActiveLogService.verifiedLog(userActiveLogId);
        return WwoossResponseUtil.responseOkNoData();
    }
}
