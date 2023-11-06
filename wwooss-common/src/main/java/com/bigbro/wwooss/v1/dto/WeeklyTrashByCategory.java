package com.bigbro.wwooss.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyTrashByCategory {

    @JsonProperty("trashName")
    private String trashName;

    @JsonProperty("trashCount")
    private Long trashCount;

    public static WeeklyTrashByCategory of(String trashName, Long trashCount) {
        return WeeklyTrashByCategory.builder()
                .trashName(trashName)
                .trashCount(trashCount)
                .build();
    }

    public void updateTrashCount(Long trashCount) {
        this.trashCount = trashCount;
    }

    @Override
    public boolean equals(Object trashName) {
        return trashName.equals(this.trashName);
    }

}
