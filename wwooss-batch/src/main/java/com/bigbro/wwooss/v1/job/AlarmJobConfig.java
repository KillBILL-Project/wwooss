package com.bigbro.wwooss.v1.job;

import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AlarmJobConfig {

    public static final String JOB_NAME = "alarmJob";
    public static final String STEP_NAME = "alarmStep";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory emf;

    private final AlarmTasklet alarmTasklet;

    @Bean
    public Job alarmJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(alarmStep())
                .build();
    }

    @Bean
    public Step alarmStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(alarmTasklet)
                .build();
    }


}
