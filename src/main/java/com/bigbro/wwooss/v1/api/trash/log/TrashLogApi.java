package com.bigbro.wwooss.v1.api.trash.log;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import com.bigbro.wwooss.v1.domain.response.trash.TrashLogListResponse;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.YearMonth;

@RestController
@RequestMapping("/v1/trash-log")
@RequiredArgsConstructor
public class TrashLogApi {

    private final TrashLogService trashLogService;

    @GetMapping
    private ResponseEntity<WwoossResponse<TrashLogListResponse>> getTrashLog(
            @RequestParam @Nullable String date,
            Pageable pageable) {
        return WwoossResponseUtil.responseOkAddData(trashLogService.getTrashLogList(1L, date, pageable));
    }
}
