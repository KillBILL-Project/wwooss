package com.bigbro.wwooss.v1.dto.response.trash;

import com.bigbro.wwooss.v1.entity.trash.category.TrashCategory;
import com.bigbro.wwooss.v1.enumType.TrashType;
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

    public static TrashCategoryResponse from(TrashCategory trashCategory) {
        return TrashCategoryResponse.builder()
                .trashCategoryId(trashCategory.getTrashCategoryId())
                .trashCategoryName(trashCategory.getTrashType().getName())
                .build();
    }
}
