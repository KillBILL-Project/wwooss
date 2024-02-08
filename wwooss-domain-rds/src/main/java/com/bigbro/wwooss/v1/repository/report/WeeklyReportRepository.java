package com.bigbro.wwooss.v1.repository.report;

import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.report.custom.WeeklyReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long>, WeeklyReportRepositoryCustom {

    Optional<WeeklyReport> findWeeklyReportByWeeklyReportIdAndUser(long weeklyReportId, User user);

    List<WeeklyReport> findAllByUser(User user);
    void deleteByUser(User user);
}
