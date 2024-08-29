package com.bigbro.wwooss.v1.scheduler;

import com.bigbro.wwooss.v1.job.DailyCreateQuizJobConfig;
import com.bigbro.wwooss.v1.job.NotionQuizDBJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 푸쉬 알림 관련 스케줄러
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuizDBScheduler {

    private final JobLauncher jobLauncher;

    private final NotionQuizDBJobConfig notionQuizDBJobConfig;
    private final DailyCreateQuizJobConfig dailyCreateQuizJobConfig;

    /**
     * 노션에서 퀴즈 가져오기
     * 매주 일요일 새벽 2시 마다
     */
    @Scheduled(cron = "0 0 2 * * 0", zone = "Asia/Seoul")
    public void weeklyNotionQuizSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters();

        jobLauncher.run(notionQuizDBJobConfig.notionQuizDBJob(), jobParameters);

    }

    /**
     * 매일 신규 유저별 퀴즈 생성
     * 매주 일요일 새벽 12시 5분 마다
     */
//    @Scheduled(cron = "0 5 12 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 10 17 * * *", zone = "Asia/Seoul")
    public void dailyCreateQuizSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters();

        jobLauncher.run(dailyCreateQuizJobConfig.dailyQuizDBJob(), jobParameters);

    }

}
