package com.bigbro.wwooss.v1.api.complimentCard;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardMetaRequest;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardMetaService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/compliment-card-meta")
@RequiredArgsConstructor
public class ComplimentCardMetaApi {

    private final ComplimentCardMetaService complimentCardMetaService;

    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> createComplimentCardMeta(@RequestBody @Valid ComplimentCardMetaRequest complimentCardMetaRequest) {
        complimentCardMetaService.createComplimentCardMeta(complimentCardMetaRequest);
        return WwoossResponseUtil.responseCreatedNoData();
    }
}
