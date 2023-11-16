package com.bigbro.wwooss.v1.job;

import static com.bigbro.wwooss.v1.entity.alarm.QAlarm.alarm;
import static java.time.LocalDateTime.parse;

import com.bigbro.wwooss.v1.batch.reader.QuerydslPagingItemReader;
import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.repository.alarm.AlarmRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AlarmJobConfig {

    @Value("${job.step.chunk.size}")
    private int chunkSize;
    public static final String JOB_NAME = "alarmJob";
    public static final String STEP_NAME = "alarmStep";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory emf;

    private final AlarmRepository alarmRepository;

    @Bean
    public Job alarmJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(alarmStep())
                .build();
    }

    @Bean
    public Step alarmStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Alarm, Void>chunk(chunkSize)
                .reader(alarmReader(null))
                .writer(alarmWriter())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<Alarm> alarmReader(@Value("#{jobParameters[date]}")  String date) {
        LocalDateTime parseDate = parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        int hour = parseDate.getHour();
        int minute = parseDate.getMinute();
        DayOfWeek dayOfWeek = parseDate.getDayOfWeek();

        return new QuerydslPagingItemReader<>(emf, chunkSize, queryFactory -> queryFactory
                .selectFrom(alarm)
                .where(alarm.sendHour.eq(hour).and(
                        alarm.sendMinute.eq(minute)).and(
                                alarm.dayOfWeekList.contains(dayOfWeek.getValue())
                )));

    }

    @Bean
    public JpaItemWriter<Void> alarmWriter() {
        return null;
    }

}
