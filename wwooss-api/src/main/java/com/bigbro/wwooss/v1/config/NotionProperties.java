package com.bigbro.wwooss.v1.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class NotionProperties {

    @Value("${notion.database-id}")
    private String databaseId;

    @Value("${notion.key}")
    private String key;

    @Value("${notion.database-query-url}")
    private String databaseQueryUrl;

    @Value("${notion.database-update-url}")
    private String databaseUpdateUrl;

    @Value("${notion.version}")
    private String version;

}
