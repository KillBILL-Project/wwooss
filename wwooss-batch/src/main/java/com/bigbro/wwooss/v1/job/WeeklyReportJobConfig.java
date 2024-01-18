package com.bigbro.wwooss.v1.job;

import static com.bigbro.wwooss.v1.entity.user.QUser.user;
import static java.time.LocalDateTime.parse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.bigbro.wwooss.v1.batch.reader.QuerydslPagingItemReader;
import com.bigbro.wwooss.v1.dto.ResultOfDiscardedTrash;
import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.dto.WowReport;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManagerFactory;

import com.bigbro.wwooss.v1.utils.TrashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WeeklyReportJobConfig {

    @Value("${job.step.chunk.size}")
    private int chunkSize;
    public static final String JOB_NAME = "weeklyReportJob";
    public static final String STEP_NAME = "weeklyReportStep";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory emf;

    private final TrashLogRepository trashLogRepository;

    private final WeeklyReportRepository weeklyReportRepository;

    @Bean
    public Job weeklyReportJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<User, WeeklyReport>chunk(chunkSize)
                .reader(reader())
                .processor(processor(null))
                .writer(writer())
                .build();
    }

    /**
     * 추후 개선 시도
     * proccessor 제거 후 바로 처리
     */
    @Bean
    @StepScope
    public ItemProcessor<User, WeeklyReport> processor(@Value("#{jobParameters[date]}")  String date) {
        LocalDateTime parseDate = parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return user -> {
            List<TrashLog> trashLogByUserAtLastWeek = trashLogRepository.findTrashLogByUserAtLastWeek(user, parseDate.toLocalDate());
            WeeklyReport report = createReport(user, trashLogByUserAtLastWeek);

            return Objects.isNull(report) ? null : weeklyReportRepository.save(report);
        };
    }

    @Bean
    public QuerydslPagingItemReader<User> reader() {
        return new QuerydslPagingItemReader<>(emf, chunkSize, queryFactory -> queryFactory
                .selectFrom(user));

    }

    @Bean
    public JpaItemWriter<WeeklyReport> writer() {
        return new JpaItemWriterBuilder<WeeklyReport>()
                .entityManagerFactory(emf)
                .build();
    }

    private WeeklyReport createReport(User user, List<TrashLog> trashLogList) {
        if (trashLogList.isEmpty()) {
            return null;
        }

        // init
        Set<Integer> attendanceSet = new HashSet<>();
        List<WeeklyTrashByCategory> weeklyTrashByCategoryList = new ArrayList<>();
        double weeklyCarbonEmission = 0;
        long weeklyRefund = 0;
        long weeklyTrashCount = 0;

        for (TrashLog trashLog : trashLogList) {
            // 출석
            attendanceSet.add(trashLog.getCreatedAt().getDayOfWeek().getValue());

            // 카테고리별 쓰레기 수
            Optional<WeeklyTrashByCategory> weeklyTrashByCategory = weeklyTrashByCategoryList.stream().filter((trash) ->
                            trash.getTrashName().equals(trashLog.getTrashInfo().getName()))
                    .findFirst();

            // 주간 버린 쓰리기 탄소배출량, 환급금
            weeklyTrashByCategory.ifPresentOrElse(
                    trashByCategory ->
                            trashByCategory.updateTrashCount(trashByCategory.getTrashCount() + trashLog.getTrashCount()),
                    () ->
                            weeklyTrashByCategoryList.add(
                                    WeeklyTrashByCategory.of(trashLog.getTrashInfo().getName(), trashLog.getTrashCount())));

            ResultOfDiscardedTrash discardedResult = TrashUtil.discardResult(trashLog.getTrashCount(),
                    trashLog.getTrashInfo().getWeight(),
                    trashLog.getSize(),
                    trashLog.getTrashInfo().getCarbonEmissionPerGram(),
                    trashLog.getTrashInfo().getRefund()
            );
            weeklyCarbonEmission += discardedResult.getCarbonEmission();
            weeklyRefund += discardedResult.getRefund();

            // 주간 버린 쓰레기 수
            weeklyTrashCount += trashLog.getTrashCount();

        }

        List<Integer> attendanceList = new ArrayList<>(attendanceSet);

        return WeeklyReport.of(attendanceList,
                weeklyTrashByCategoryList,
                weeklyCarbonEmission,
                weeklyRefund,
                weeklyTrashCount,
                getWowResult(
                        user,
                        weeklyCarbonEmission,
                        weeklyRefund,
                        weeklyTrashCount),
                // 전 주에 대한 값을 월요일에 생성하기 때문에 전 주날을 기록하여 해당 주차와 주간을 맞춤 + 조회할 떄 편의를 위함
                LocalDateTime.now().minusDays(7),
                user);
    }

    // 전주 대비 증감률
    private WowReport getWowResult(User user,
                                   double weeklyCarbonEmission,
                                   long weeklyRefund,
                                   long weeklyTrashCount) {
        WeeklyReport lastWeekReport = weeklyReportRepository.findWeeklyReportByUserAtLastWeek(user);

        if (Objects.isNull(lastWeekReport)) {
            return WowReport.zeroReport();
        }

        double changedCarbonEmission = weeklyCarbonEmission - lastWeekReport.getWeeklyCarbonEmission();
        long changedRefund = weeklyRefund - lastWeekReport.getWeeklyRefund();

        long lastTrashCount = lastWeekReport.getWeeklyTrashCount();
        long changedTrashCountPercent =((weeklyTrashCount - lastTrashCount) / lastTrashCount) * 100;

        return WowReport.of(changedCarbonEmission, changedRefund, changedTrashCountPercent);
    }
}
