package com.bigbro.wwooss.v1.job;

import com.bigbro.wwooss.v1.repository.alarm.AlarmRepository;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
    public Tasklet alarmTasklet() {
        return new AlarmTasklet(alarmRepository);
    }

    @Bean
    public Step alarmStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(alarmTasklet())
                .build();
    }


}
