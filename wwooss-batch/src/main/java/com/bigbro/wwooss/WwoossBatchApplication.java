package com.bigbro.wwooss;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableBatchProcessing
@ComponentScan(basePackages={"com.bigbro.wwooss", "org.springframework.batch.core.configuration.annotation"})
@EnableAutoConfiguration
public class WwoossBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WwoossBatchApplication.class, args);
    }

}
