package com.bigbro.wwooss.v1.scheduler;

import com.bigbro.wwooss.v1.job.WeeklyReportJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportScheduler {

    private final JobLauncher jobLauncher;

    private final WeeklyReportJobConfig weeklyReportJobConfig;

    /**
     * 주간 쓰레기 로그 리포트
     * 매주 월요일 새벽 3시 마다 전주( 월 ~ 일 ) 로그 데이터 리포팅
     */
    @Scheduled(cron = "0 0 3 * * 1", zone = "Asia/Seoul")
    public void weeklyReportSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        log.info("매주 월요일 새벽 3시");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters();

        jobLauncher.run(weeklyReportJobConfig.weeklyReportJob(), jobParameters);

    }

}
