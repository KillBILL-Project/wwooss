package com.bigbro.wwooss.v1.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeekInfo {

    private int year;

    private int month;

    private int weekOfMonth;

    public static WeekInfo of(int year, int month, int weekOfMonth) {
        return WeekInfo.builder()
                .year(year)
                .month(month)
                .weekOfMonth(weekOfMonth)
                .build();
    }

}
