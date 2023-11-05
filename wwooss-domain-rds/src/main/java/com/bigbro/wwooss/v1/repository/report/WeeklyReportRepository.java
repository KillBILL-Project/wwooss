package com.bigbro.wwooss.v1.repository.report;

import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Slice;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    WeeklyReport findWeeklyReportByWeekNumber(Long weekNumber);

    Slice<WeeklyReport> findWeeklyReportByUser(User user, Pageable pageable);

    WeeklyReport findWeeklyReportByWeeklyReportIdAndUser(long weeklyReportId, User user);
}
