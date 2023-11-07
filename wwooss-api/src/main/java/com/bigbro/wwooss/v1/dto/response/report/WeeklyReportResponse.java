package com.bigbro.wwooss.v1.dto.response.report;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class WeeklyReportResponse {

    private long weeklyReportId;

    private long weekNumber;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    public static WeeklyReportResponse of(long weeklyReportId, long weekNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        return WeeklyReportResponse.builder()
                .weeklyReportId(weeklyReportId)
                .weekNumber(weekNumber)
                .fromDate(fromDate)
                .toDate(toDate)
                .build();
    }
}
