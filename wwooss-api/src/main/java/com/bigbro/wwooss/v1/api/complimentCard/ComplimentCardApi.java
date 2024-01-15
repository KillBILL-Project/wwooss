package com.bigbro.wwooss.v1.api.complimentCard;

import com.bigbro.wwooss.v1.dto.request.complimentCard.ComplimentCardRequest;
import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.complimentCard.ComplimentCardListResponse;
import com.bigbro.wwooss.v1.enumType.CardType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.complimentCard.ComplimentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/compliment-card")
@RequiredArgsConstructor
public class ComplimentCardApi {

    private final ComplimentCardService complimentCardService;


    /**
     * 내부 API
     *
     * 칭찬 카드 생성
     */
    @PostMapping
    public ResponseEntity<WwoossResponse<Void>> createComplimentCard(@RequestBody ComplimentCardRequest complimentCardRequest) {
        complimentCardService.createComplimentCard(complimentCardRequest);
        return WwoossResponseUtil.responseCreatedNoData();
    }

    @GetMapping
    public ResponseEntity<WwoossResponse<ComplimentCardListResponse>> getComplimentCard(
            @RequestParam("card-type") CardType cardType,
            Pageable pageable, UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(complimentCardService.getComplimentCardList(userCredential.getUserId(), cardType, pageable));
    }
}
