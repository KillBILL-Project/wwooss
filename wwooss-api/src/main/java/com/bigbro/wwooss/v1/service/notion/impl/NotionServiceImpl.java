package com.bigbro.wwooss.v1.service.notion.impl;

import com.bigbro.wwooss.v1.client.NotionClient;
import com.bigbro.wwooss.v1.dto.request.notion.QuizDBCursorRequest;
import com.bigbro.wwooss.v1.dto.request.notion.QuizDBRequest;
import com.bigbro.wwooss.v1.dto.request.notion.QuizFilter;
import com.bigbro.wwooss.v1.dto.request.notion.QuizStatus;
import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResponse;
import com.bigbro.wwooss.v1.enumType.QuizDBQueryType;
import com.bigbro.wwooss.v1.service.notion.NotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotionServiceImpl implements NotionService {

    private final NotionClient notionClient;

    private final Integer PAGE_SIZE = 10;

    @Override
    public QuizDBResponse getNotionQuizDB(String nextCursor) {
        QuizFilter status = QuizFilter.of("status", QuizStatus.from(QuizDBQueryType.NOT_START.getValue()));
        return Objects.isNull(nextCursor) ? notionClient.request(QuizDBRequest.of(PAGE_SIZE, status))
                : notionClient.request(QuizDBCursorRequest.of(PAGE_SIZE, status, nextCursor));
    }

    @Override
    public void updateStatusDone(String pageId) {
        notionClient.updateStatusDone(pageId);
    }
}
