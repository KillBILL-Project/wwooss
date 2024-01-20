package com.bigbro.wwooss.v1.api.trash.can;

import com.bigbro.wwooss.v1.dto.response.trash.TrashCanResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.es.TrashCanDocumentService;
import com.bigbro.wwooss.v1.service.trash.can.TrashCanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/trash-can")
@RequiredArgsConstructor
public class TrashCanApi {

    private final TrashCanDocumentService trashCanDocumentService;

    @GetMapping
    ResponseEntity<WwoossResponse<List<TrashCanResponse>>> getTrashCanAroundMe(@RequestParam("lat") Double lat,
                                                                              @RequestParam("lng") Double lng,
                                                                              @RequestParam("trashType") String trashType) {
        return WwoossResponseUtil.responseOkAddData(trashCanDocumentService.getTrashCanAroundMe(lat, lng, trashType));
    }
}
