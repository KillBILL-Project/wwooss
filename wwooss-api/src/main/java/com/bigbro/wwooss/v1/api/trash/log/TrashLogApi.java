package com.bigbro.wwooss.v1.api.trash.log;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.trash.TrashLogListResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.trash.log.TrashLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trash-log")
@RequiredArgsConstructor
public class TrashLogApi {

    private final TrashLogService trashLogService;

    @GetMapping
    private ResponseEntity<WwoossResponse<TrashLogListResponse>> getTrashLog(
            @RequestParam @Nullable String date,
            Pageable pageable, UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(trashLogService.getTrashLogList(userCredential.getUserId(), date,
                pageable));
    }
}
