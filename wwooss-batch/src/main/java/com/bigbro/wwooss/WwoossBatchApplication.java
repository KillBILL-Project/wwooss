package com.bigbro.wwooss;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;

@EnableBatchProcessing
public class WwoossBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WwoossBatchApplication.class, args);
    }

}
