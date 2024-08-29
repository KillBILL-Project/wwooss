package com.bigbro.wwooss.v1.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyCreateQuizJobConfig {

    public static final String JOB_NAME = "dailyQuizJob";
    public static final String STEP_NAME = "dailyQuizDBStep";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory emf;

    private final DailyCreateQuizTasklet dailyCreateQuizTasklet;

    @Bean
    public Job dailyQuizDBJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(dailyQuizDBStep())
                .build();
    }

    @Bean
    public Step dailyQuizDBStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(dailyCreateQuizTasklet)
                .build();
    }


}
