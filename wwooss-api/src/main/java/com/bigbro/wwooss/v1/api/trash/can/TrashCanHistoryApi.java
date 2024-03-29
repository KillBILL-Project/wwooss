package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanHistoryListResponse;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/trash-can-histories")
@RequiredArgsConstructor
public class TrashCanHistoryApi {

    private final TrashCanHistoryService trashCanHistoryService;

    private final ComplimentConditionLogService complimentConditionLogService;

    private final ComplimentCardService complimentCardService;

    @GetMapping
    public ResponseEntity<WwoossResponse<TrashCanHistoryListResponse>> findTrashCanHistoryList(@RequestParam @Nullable String date,
            Pageable pageable, UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(trashCanHistoryService.findTrashCanHistoryList(userCredential.getUserId(), date,
                pageable));
    }

    @GetMapping("/{trash-can-history-id}")
    public ResponseEntity<WwoossResponse<EmptyTrashResultResponse>> findTrashCanHistoryDetail(@PathVariable("trash-can-history-id") Long trashCanHistoryId,
                                                                                              UserCredential userCredential) {
        boolean isCreateComplimentCard = complimentConditionLogService.createViewTrashCanHistoryLog(userCredential.getUserId());
        if(isCreateComplimentCard) {
            complimentCardService.createComplimentCard(ComplimentCardRequest.of(userCredential.getUserId(), ComplimentType.CLEAN_TRASH_CAN));
        }
        return WwoossResponseUtil.responseOkAddData(trashCanHistoryService.findTrashCanHistoryDetail(userCredential.getUserId(), trashCanHistoryId));
    }

}
