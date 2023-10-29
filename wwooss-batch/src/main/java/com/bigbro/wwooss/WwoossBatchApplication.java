package com.bigbro.wwooss;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
public class WwoossBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WwoossBatchApplication.class, args);
    }

}
