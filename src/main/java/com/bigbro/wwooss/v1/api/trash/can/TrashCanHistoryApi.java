package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.common.WwoossResponse;
import com.bigbro.wwooss.v1.common.WwoossResponseUtil;
import com.bigbro.wwooss.v1.domain.response.trash.TrashCanHistoryListResponse;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trash-can-histories")
@RequiredArgsConstructor
public class TrashCanHistoryApi {

    private final TrashCanHistoryService trashCanHistoryService;

    @GetMapping
    public ResponseEntity<WwoossResponse<TrashCanHistoryListResponse>> findTrashCanHistoryList(@PageableDefault(sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // TODO : userId 추가
        return WwoossResponseUtil.responseOkAddData(trashCanHistoryService.findTrashCanHistoryList(1L, pageable));
    }
}
