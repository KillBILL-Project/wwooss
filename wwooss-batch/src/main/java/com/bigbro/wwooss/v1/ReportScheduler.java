package com.bigbro.wwooss.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportScheduler {

    /**
     * 주간 쓰레기 로그 리포트
     * 매주 월요일 새벽 3시 마다 전주( 월 ~ 일 ) 로그 데이터 리포팅
     */
    @Scheduled(cron = "0 0 3 * * 1", zone = "Asia/Seoul")
    public void weeklyReportSchedule() {
        log.info("매주 월요일 새벽 3시");
    }
}
