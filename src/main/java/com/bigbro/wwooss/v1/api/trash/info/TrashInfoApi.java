package com.bigbro.wwooss.v1.api.trash.info;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import com.bigbro.wwooss.v1.domain.request.trash.info.TrashInfoRequest;
import com.bigbro.wwooss.v1.domain.response.trash.TrashInfoResponse;
import com.bigbro.wwooss.v1.service.trash.info.TrashInfoService;
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
    public ResponseEntity<WwoossResponse<List<TrashInfoResponse>>> getTrashInfoByCategoryId(@RequestParam(value = "categoryId") Long categoryId) {
        return WwoossResponseUtil.responseOkAddData(trashInfoService.getTrashInfoByCategoryId(categoryId));
    }

    /**
     * 내부 API
     * 쓰레기 정보 생성 api
     */
    @PostMapping
    public ResponseEntity<WwoossResponse<TrashInfoResponse>> createTrashInfo(@RequestBody @Valid TrashInfoRequest trashInfoRequest) {
        return WwoossResponseUtil.responseCreatedAddData(trashInfoService.createTrashInfo(trashInfoRequest));
    }
}
