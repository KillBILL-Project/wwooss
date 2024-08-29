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
public class NotionQuizDBJobConfig {

    public static final String JOB_NAME = "notionQuizDBJob";
    public static final String STEP_NAME = "notionQuizDBStep";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory emf;

    private final NotionQuizDBTasklet notionQuizDBTasklet;

    @Bean
    public Job notionQuizDBJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(notionQuizDBStep())
                .build();
    }

    @Bean
    public Step notionQuizDBStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(notionQuizDBTasklet)
                .build();
    }


}
