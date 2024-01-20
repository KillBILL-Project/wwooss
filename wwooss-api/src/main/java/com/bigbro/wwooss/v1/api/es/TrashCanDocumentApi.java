package com.bigbro.wwooss.v1.api.es;

import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.es.TrashCanDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trash-can/document")
@RequiredArgsConstructor
public class TrashCanDocumentApi {

    private final TrashCanDocumentService trashCanDocumentService;

    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> migrationDocument() {
        trashCanDocumentService.migrationTrashCanDocument();
        return WwoossResponseUtil.responseCreatedNoData();
    }
}
