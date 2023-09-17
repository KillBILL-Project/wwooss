package com.bigbro.killbill.v1.domain.response.trash;

import com.bigbro.killbill.v1.domain.entity.trash.TrashCategoryEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TrashCategoryResponse {

    private Long trashCategoryId;

    private String trashCategoryName;

    public static TrashCategoryResponse from(TrashCategoryEntity trashCategoryEntity) {
        return TrashCategoryResponse.builder()
                .trashCategoryId(trashCategoryEntity.getTrashCategoryId())
                .trashCategoryName(trashCategoryEntity.getTrashCategoryName())
                .build();
    }
}
