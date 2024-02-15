package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.trash.TrashCanResponse;
import com.bigbro.wwooss.v1.enumType.ComplimentType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentConditionLogService;
import com.bigbro.wwooss.v1.service.es.TrashCanDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/trash-can")
@RequiredArgsConstructor
public class TrashCanApi {

    private final TrashCanDocumentService trashCanDocumentService;

    private final ComplimentConditionLogService complimentConditionLogService;

    private final ComplimentCardService complimentCardService;

    @GetMapping
    ResponseEntity<WwoossResponse<List<TrashCanResponse>>> getTrashCanAround(@RequestParam("lat") Double lat,
                                                                             @RequestParam("lng") Double lng,
                                                                             @RequestParam("distance") Integer distance,
                                                                             @RequestParam(value = "trashType", required = false) String trashType,
                                                                             UserCredential userCredential) {
        boolean isCreateFindComplimentCard = complimentConditionLogService.createFindTrashCanLog(userCredential.getUserId());
        if(isCreateFindComplimentCard) {
            complimentCardService.createComplimentCard(ComplimentCardRequest.of(userCredential.getUserId(), ComplimentType.FIND_TRASH_CAN));
        }
        return WwoossResponseUtil.responseOkAddData(trashCanDocumentService.findTrashCanByGeoLocation(lat, lng, distance, trashType));
    }

}
