package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.dto.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/trash-can-contents")
@RequiredArgsConstructor
public class TrashCanContentsApi {

    private final TrashCanContentsService trashCanContentsService;

    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> createTrashCanContents(@RequestBody @Valid TrashCanContentsRequest trashCanContentsRequest) {
        // TODO : UserId 넣기
        trashCanContentsService.createTrashCanContents(trashCanContentsRequest, 1L);

        return WwoossResponseUtil.responseCreatedNoData();
    }

    @DeleteMapping
    public ResponseEntity<WwoossResponse<EmptyTrashResultResponse>> deleteTrashCanContentsList() {
        // TODO : userId 넣기
        return WwoossResponseUtil.responseOkAddData(trashCanContentsService.deleteTrashCanContents(1L));
    }
}
