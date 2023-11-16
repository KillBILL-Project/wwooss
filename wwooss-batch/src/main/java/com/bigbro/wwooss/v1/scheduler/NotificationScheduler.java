package com.bigbro.wwooss.v1.scheduler;

import com.bigbro.wwooss.v1.job.AlarmJobConfig;
import java.time.LocalDateTime;
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

/**
 * 푸쉬 알림 관련 스케줄러
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {

    private final JobLauncher jobLauncher;

    private final AlarmJobConfig alarmJobConfig;

    /**
     * 알람 스캐줄러
     * 매분 저장된 알람에 맞춰 알림 발송
     */
    @Scheduled(cron = "0 * * * * ?", zone = "Asia/Seoul")
    public void weeklyReportSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        log.info("매분 알람 발송");

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters();

        jobLauncher.run(alarmJobConfig.alarmJob(), jobParameters);

    }

}
