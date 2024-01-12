package com.bigbro.wwooss;

import com.bigbro.wwooss.v1.factory.YamlPropertySourceFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
@PropertySource(value = "classpath:application-batch.yaml", factory = YamlPropertySourceFactory.class)
public class WwoossBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(WwoossBatchApplication.class, args);
    }

}
