package com.bigbro.wwooss.v1.client;


import com.bigbro.wwooss.v1.config.NotionProperties;
import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NotionClient {

    private final NotionProperties notionProperties;

    public<T> QuizDBResponse request(T quizDBRequest) {

        HttpEntity<T> entity = new HttpEntity<>(quizDBRequest, getHeaders());

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String url = UriComponentsBuilder.fromHttpUrl(notionProperties.getDatabaseQueryUrl())
                .buildAndExpand(notionProperties.getDatabaseId())
                .toUriString();

        ResponseEntity<QuizDBResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, QuizDBResponse.class);
        return response.getBody();
    }

    public void updateStatusDone(String pageId) {
        Map<String, Object> select = new HashMap<>();
        select.put("name", "Done");

        Map<String, Object> property = new HashMap<>();
        property.put("status", select);

        Map<String, Object> properties = new HashMap<>();
        properties.put("status", property);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("properties", properties);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, getHeaders());

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String url = UriComponentsBuilder.fromHttpUrl(notionProperties.getDatabaseUpdateUrl())
                .buildAndExpand(pageId)
                .toUriString();
        restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
    }

    private HttpHeaders getHeaders() {
        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + notionProperties.getKey());
        headers.set("Notion-Version", notionProperties.getVersion());
        headers.set("Content-Type", "application/json");
        return  headers;
    }

}
