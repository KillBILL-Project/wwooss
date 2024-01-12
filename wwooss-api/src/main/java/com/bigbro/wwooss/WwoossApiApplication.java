package com.bigbro.wwooss;

import com.bigbro.wwooss.v1.factory.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class WwoossApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WwoossApiApplication.class, args);
    }

}
