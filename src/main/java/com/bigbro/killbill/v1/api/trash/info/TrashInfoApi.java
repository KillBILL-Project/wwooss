package com.bigbro.killbill.v1.api.trash.info;

import com.bigbro.killbill.v1.common.KillBillResponse;
import com.bigbro.killbill.v1.common.KillBillResponseUtil;
import com.bigbro.killbill.v1.domain.request.trash.info.TrashInfoRequest;
import com.bigbro.killbill.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.killbill.v1.service.trash.info.TrashInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/trash-info")
@RequiredArgsConstructor
public class TrashInfoApi {

    private final TrashInfoService trashInfoService;

    @GetMapping
    public ResponseEntity<KillBillResponse<List<TrashInfoResponse>>> getTrashInfoByCategoryId(@RequestParam(value = "category-id") Long categoryId) {
        return ResponseEntity.ok(KillBillResponseUtil.responseOkAddData(trashInfoService.getTrashInfoByCategoryId(categoryId)));
    }

    /**
     * 내부 API
     * 쓰레기 정보 생성 api
     */
    @PostMapping
    public ResponseEntity<KillBillResponse<TrashInfoResponse>> createTrashInfo(@RequestBody @Valid TrashInfoRequest trashInfoRequest) {
        return ResponseEntity.ok(KillBillResponseUtil.responseOkAddData(trashInfoService.createTrashInfo(trashInfoRequest)));
    }
}
