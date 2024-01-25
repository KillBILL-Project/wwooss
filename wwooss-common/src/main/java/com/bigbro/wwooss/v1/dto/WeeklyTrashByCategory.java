package com.bigbro.wwooss.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyTrashByCategory {

    @JsonProperty("trashCategoryName")
    private String trashCategoryName;

    @JsonProperty("trashCount")
    private Long trashCount;

    public static WeeklyTrashByCategory of(String trashCategoryName, Long trashCount) {
        return WeeklyTrashByCategory.builder()
                .trashCategoryName(trashCategoryName)
                .trashCount(trashCount)
                .build();
    }

    public void updateTrashCount(Long trashCount) {
        this.trashCount = trashCount;
    }

    @Override
    public boolean equals(Object trashCategoryName) {
        return trashCategoryName.equals(this.trashCategoryName);
    }

}
