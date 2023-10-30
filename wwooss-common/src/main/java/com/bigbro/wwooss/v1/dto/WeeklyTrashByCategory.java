package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeeklyTrashByCategory {

    private String trashName;

    private Long trashCount;

}
