package com.bigbro.wwooss.v1.repository.report.custom;

import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WeeklyReportRepositoryCustom {

    WeeklyReport findWeeklyReportByUserAtLastWeek(User user);

    Slice<WeeklyReport> findWeeklyReportByUserAtDate(String date, User user, Pageable pageable);

}
