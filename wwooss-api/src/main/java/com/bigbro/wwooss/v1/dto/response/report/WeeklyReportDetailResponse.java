package com.bigbro.wwooss.v1.dto.response.report;

import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class WeeklyReportDetailResponse {

    // report id
    private Long weeklyReportId;

    // 출석 요일 [1 ~ 7 / 월 ~ 일]
    private List<Integer> attendanceRecord;

    // 주간 탄소 절감량
    private Double weeklyCarbonSaving;

    // 주간 환급금
    private Long weeklyRefund;

    // 주간 버린 쓰레기
    private Long weeklyTrashCount;

    // 전주 대비 탄소 절감량 증감 Week On Week
    private Double wowCarbonSaving;

    // 전주 대비 환불 증감 Week On Week
    private Long wowRefund;

    // 전주 대비 버린 쓰레기 수 증감 Week On Week
    private Long wowTrashCount;

    // 카테고리별 버린 쓰레기 수
    private List<WeeklyTrashByCategory> weeklyTrashCountByCategoryList;

    public static WeeklyReportDetailResponse from(WeeklyReport weeklyReport) {
        return WeeklyReportDetailResponse.builder()
                .weeklyReportId(weeklyReport.getWeeklyReportId())
                .attendanceRecord(weeklyReport.getAttendanceRecord())
                .weeklyCarbonSaving(weeklyReport.getWeeklyCarbonSaving())
                .weeklyRefund(weeklyReport.getWeeklyRefund())
                .weeklyTrashCount(weeklyReport.getWeeklyTrashCount())
                .weeklyTrashCountByCategoryList(weeklyReport.getWeeklyTrashCountByCategoryList())
                .wowCarbonSaving(weeklyReport.getWowCarbonSaving())
                .wowRefund(weeklyReport.getWowRefund())
                .wowTrashCount(weeklyReport.getWowTrashCount())
                .build();
    }
}
