package com.bigbro.wwooss.v1.dto.response.report;

import com.bigbro.wwooss.v1.dto.WeekInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class WeeklyReportResponse {

    private long weeklyReportId;

    private WeekInfo weekInfo;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    public static WeeklyReportResponse of(long weeklyReportId, WeekInfo weekInfo, LocalDateTime fromDate,
            LocalDateTime toDate) {
        return WeeklyReportResponse.builder()
                .weeklyReportId(weeklyReportId)
                .weekInfo(weekInfo)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
    }
}
