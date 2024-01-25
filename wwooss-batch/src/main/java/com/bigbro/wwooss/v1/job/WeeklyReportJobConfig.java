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
import java.util.*;
import javax.persistence.EntityManagerFactory;

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
        Map<String, Long> weeklyTrashMap = new HashMap<>();
        double weeklyCarbonSaving = 0;
        long weeklyRefund = 0;
        long weeklyTrashCount = 0;

        for (TrashLog trashLog : trashLogList) {
            // 출석
            attendanceSet.add(trashLog.getCreatedAt().getDayOfWeek().getValue());

            // 카테고리별 쓰레기
            String trashCategoryName = trashLog.getTrashInfo().getTrashCategory().getTrashType().getName();
            weeklyTrashMap.put(trashCategoryName, weeklyTrashMap.getOrDefault(trashCategoryName, 0L) + 1);

            weeklyCarbonSaving += trashLog.getTrashInfo().getCarbonSaving();
            weeklyRefund += trashLog.getTrashInfo().getRefund();

            // 주간 버린 쓰레기 수
            ++weeklyTrashCount;

        }

        List<WeeklyTrashByCategory> weeklyTrashByCategoryList = weeklyTrashMap.keySet().stream()
                .map(key -> WeeklyTrashByCategory.of(key, weeklyTrashMap.get(key))).toList();

        List<Integer> attendanceList = new ArrayList<>(attendanceSet);

        return WeeklyReport.of(attendanceList,
                weeklyTrashByCategoryList,
                weeklyCarbonSaving,
                weeklyRefund,
                weeklyTrashCount,
                getWowResult(
                        user,
                        weeklyCarbonSaving,
                        weeklyRefund,
                        weeklyTrashCount),
                // 전 주에 대한 값을 월요일에 생성하기 때문에 전 주날을 기록하여 해당 주차와 주간을 맞춤 + 조회할 떄 편의를 위함
                LocalDateTime.now().minusDays(7),
                user);
    }

    // 전주 대비 증감률
    private WowReport getWowResult(User user,
                                   double weeklyCarbonSaving,
                                   long weeklyRefund,
                                   long weeklyTrashCount) {
        WeeklyReport lastWeekReport = weeklyReportRepository.findWeeklyReportByUserAtLastWeek(user);

        if (Objects.isNull(lastWeekReport)) {
            return WowReport.zeroReport();
        }

        double changedCarbonSaving = weeklyCarbonSaving - lastWeekReport.getWeeklyCarbonSaving();
        long changedRefund = weeklyRefund - lastWeekReport.getWeeklyRefund();

        long lastTrashCount = lastWeekReport.getWeeklyTrashCount();
        long changedTrashCountPercent =((weeklyTrashCount - lastTrashCount) / lastTrashCount) * 100;

        return WowReport.of(changedCarbonSaving, changedRefund, changedTrashCountPercent);
    }
}
