package com.bigbro.wwooss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"classpath:application-rds.yaml"})
public class WwoossApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WwoossApiApplication.class, args);
    }

}
