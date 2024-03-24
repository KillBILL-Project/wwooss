package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.dto.request.trash.can.TrashCanContentsRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.trash.EmptyTrashResultResponse;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import com.bigbro.wwooss.v1.enumType.NotificationTemplateCode;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanContentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/trash-can-contents")
@RequiredArgsConstructor
public class TrashCanContentsApi {

    private final TrashCanContentsService trashCanContentsService;

    private final ComplimentConditionLogService complimentConditionLogService;

    private final ComplimentCardService complimentCardService;

    private final NotificationService notificationService;

    @GetMapping("/total-count")
    public ResponseEntity<WwoossResponse<Long>> getTrashCanContentsCount(UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(trashCanContentsService.getTrashCanContentsCount(userCredential.getUserId()));
    }

    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> createTrashCanContents(@RequestBody @Valid TrashCanContentsRequest trashCanContentsRequest, UserCredential userCredential) {
        User user = trashCanContentsService.createTrashCanContents(trashCanContentsRequest, userCredential.getUserId());
        boolean is3InARow = complimentConditionLogService.createThrowTrashLog(userCredential.getUserId());
        if(is3InARow) {
            complimentCardService.createComplimentCard(ComplimentCardRequest.of(userCredential.getUserId(), ComplimentType.THROW_TRASH));
            notificationService.sendOne(NotificationSendRequest.of(List.of(user), NotificationTemplateCode.THROW_TRASH_COMPLIMENT_CARD));
        }

        return WwoossResponseUtil.responseCreatedNoData();
    }

    @DeleteMapping
    public ResponseEntity<WwoossResponse<EmptyTrashResultResponse>> deleteTrashCanContentsList(UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(trashCanContentsService.deleteTrashCanContents(userCredential.getUserId()));
    }
}
