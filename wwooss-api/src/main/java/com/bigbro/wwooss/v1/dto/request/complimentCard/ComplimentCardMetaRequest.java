package com.bigbro.wwooss.v1.dto.request.complimentCard;

import com.bigbro.wwooss.v1.enumType.CardCode;
import com.bigbro.wwooss.v1.enumType.CardType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder
public class ComplimentCardMetaRequest {

    @NotNull(message = "칭찬 카드 제목은 필수입니다.")
    private String title;

    @NotNull(message = "칭찬 카드 내용은 필수입니다.")
    private String contents;

    @NotNull(message = "칭찬 카드 타입은 필수 입니다 - [주간 - WEEKLY/ 통합 - INTEGRATE]")
    private CardType cardType;

    @NotNull(message = "칭찬 카드 코드는 필수 입니다. 노션 참고")
    private CardCode cardCode;

    @NotNull(message = "칭찬 카드 이미지는 필수 입니다. S3 참고")
    private String cardImage;

}
