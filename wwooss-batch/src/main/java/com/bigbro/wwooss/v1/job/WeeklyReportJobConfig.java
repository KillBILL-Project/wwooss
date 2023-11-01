package com.bigbro.wwooss.v1.job;

import static com.bigbro.wwooss.v1.entity.user.QUser.user;

import com.bigbro.wwooss.v1.batch.reader.QuerydslPagingItemReader;
import com.bigbro.wwooss.v1.entity.report.WeeklyReport;
import com.bigbro.wwooss.v1.entity.trash.log.TrashLog;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.report.WeeklyReportRepository;
import com.bigbro.wwooss.v1.repository.trash.log.TrashLogRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

    private final QuerydslPagingItemReader jobParameter;

    private final TrashLogRepository trashLogRepository;

    private final WeeklyReportRepository weeklyReportRepository;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<User, WeeklyReport>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    /**
     * 추후 개선 시도
     * proccessor 제거 후 바로 처리
     */
    @Bean
    public ItemProcessor<User, WeeklyReport> processor() {
        return new ItemProcessor<User, WeeklyReport>() {
            @Override
            public WeeklyReport process(User user) {
                LocalDate date = LocalDate.parse("2023-11-01");

                List<TrashLog> trashLogByUserAtLastWeek = trashLogRepository.findTrashLogByUserAtLastWeek(user, date);
                WeeklyReport report = createReport(user, trashLogByUserAtLastWeek);

                return Objects.isNull(report) ? null : weeklyReportRepository.save(report);
            }
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
        Set<Integer> attendanceSet = new HashSet<>();

        for(int i = 0; i < trashLogList.size(); i++) {
            TrashLog trashLog = trashLogList.get(i);
            attendanceSet.add(trashLog.getCreatedAt().getDayOfWeek().getValue());
        }

        List<Integer> attendanceList = new ArrayList<>(attendanceSet);
        return WeeklyReport.of(attendanceList, user);
    }
}
