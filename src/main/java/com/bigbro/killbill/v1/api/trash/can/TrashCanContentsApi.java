package com.bigbro.killbill.v1.api.trash.can;

import com.bigbro.killbill.v1.common.KillBillResponse;
import com.bigbro.killbill.v1.common.KillBillResponseUtil;
import com.bigbro.killbill.v1.domain.request.trash.can.TrashCanContentsRequest;
import com.bigbro.killbill.v1.service.trash.can.TrashCanContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/trash-can-contents")
@RequiredArgsConstructor
public class TrashCanContentsApi {

    private final TrashCanContentsService trashCanContentsService;

    @PostMapping
    public ResponseEntity<KillBillResponse<Void>> createTrashCanContents(@RequestBody @Valid TrashCanContentsRequest trashCanContentsRequest) {
        // TODO : UserId 넣기
        trashCanContentsService.createTrashCanContents(trashCanContentsRequest, 1L);

        return KillBillResponseUtil.responseCreatedNoData();
    }
}
