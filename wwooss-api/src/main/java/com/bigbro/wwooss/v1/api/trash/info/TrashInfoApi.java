package com.bigbro.wwooss.v1.api.trash.info;

import com.bigbro.wwooss.v1.dto.request.trash.info.TrashInfoRequest;
import com.bigbro.wwooss.v1.dto.response.trash.TrashInfoResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
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
    public ResponseEntity<WwoossResponse<List<TrashInfoResponse>>> getTrashInfoMeta() {
        return WwoossResponseUtil.responseOkAddData(trashInfoService.getTrashInfo());
    }

    /**
     * 내부 API
     * 쓰레기 정보 생성 api
     */
    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> createTrashInfo(@RequestBody @Valid TrashInfoRequest trashInfoRequest) {
        trashInfoService.createTrashInfo(trashInfoRequest);
        return WwoossResponseUtil.responseCreatedNoData();
    }
}
