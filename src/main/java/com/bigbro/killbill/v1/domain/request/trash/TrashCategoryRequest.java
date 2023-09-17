package com.bigbro.killbill.v1.domain.request.trash;

import lombok.*;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryRequest {

    @NotNull(message = "쓰레기 카테고리 이름은 필수입니다.")
    private String categoryName;

    // TODO : 이미지 추가
}
