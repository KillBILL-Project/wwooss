package com.bigbro.wwooss.v1.service.report;

import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportDetailResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportListResponse;
import org.springframework.data.domain.Pageable;

public interface WeeklyReportService {

    /**
     * 주간 리포트 목록 조회
     * @param userId 유저 ID
     * @param pageable 페이지 정보
     *
     * @return WeeklyReportListResponse
     */
    WeeklyReportListResponse getWeeklyReport(Long userId, Pageable pageable);

    /**
     * 주간 리포트 상세 조회
     * @param weeklyReportId 주간 리포트 ID
     * @param userId 유저 ID
     *
     * @return WeeklyReportDetailResponse 주간 리포트 상세 정보
     */
    WeeklyReportDetailResponse getWeeklyReportDetail(long weeklyReportId, long userId);

}
