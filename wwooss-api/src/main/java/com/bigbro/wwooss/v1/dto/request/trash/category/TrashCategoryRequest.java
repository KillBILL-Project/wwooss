package com.bigbro.wwooss.v1.dto.request.trash.category;

import com.bigbro.wwooss.v1.enumType.TrashType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryRequest {

    @NotNull(message = "쓰레기 카테고리 이름은 필수입니다.")
    private TrashType trashType;

    // TODO : 이미지 추가
}
