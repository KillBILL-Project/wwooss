package com.bigbro.wwooss.v1.dto.response.report;

import com.bigbro.wwooss.v1.dto.ComplimentCardIcon;
import com.bigbro.wwooss.v1.dto.WeekInfo;
import java.util.List;
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

    private List<ComplimentCardIcon> complimentCardIconList;

    public static WeeklyReportResponse of(long weeklyReportId, WeekInfo weekInfo, LocalDateTime fromDate,
            LocalDateTime toDate, List<ComplimentCardIcon> complimentCardIconList) {
        return WeeklyReportResponse.builder()
                .weeklyReportId(weeklyReportId)
                .weekInfo(weekInfo)
                .fromDate(fromDate)
                .toDate(toDate)
                .complimentCardIconList(complimentCardIconList)
                .build();
    }
}
