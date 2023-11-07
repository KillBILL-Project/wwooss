package com.bigbro.wwooss.v1.dto.response.report;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class WeeklyReportListResponse {

    private boolean hasNext;

    private List<WeeklyReportResponse> weeklyReportResponseList;

    public static WeeklyReportListResponse of(boolean hasNext, List<WeeklyReportResponse> weeklyReportResponseList) {
        return WeeklyReportListResponse.builder()
                .hasNext(hasNext)
                .weeklyReportResponseList(weeklyReportResponseList)
                .build();
    }

}
