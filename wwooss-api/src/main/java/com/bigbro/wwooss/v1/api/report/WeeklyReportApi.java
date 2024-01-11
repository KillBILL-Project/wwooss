package com.bigbro.wwooss.v1.api.report;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportDetailResponse;
import com.bigbro.wwooss.v1.dto.response.report.WeeklyReportListResponse;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.report.WeeklyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/weekly-reports")
@RequiredArgsConstructor
public class WeeklyReportApi {

    private final WeeklyReportService weeklyReportService;

    @GetMapping
    public ResponseEntity<WwoossResponse<WeeklyReportListResponse>> getWeeklyReportList(Pageable pageable,
            UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(weeklyReportService.getWeeklyReport(userCredential.getUserId(), pageable));
    }

    @GetMapping("/{weekly-report-id}")
    public ResponseEntity<WwoossResponse<WeeklyReportDetailResponse>> getWeeklyReportDetail(@PathVariable("weekly"
            + "-report-id") Long weeklyReportId, UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(weeklyReportService.getWeeklyReportDetail(weeklyReportId,
                userCredential.getUserId()));
    }
}
