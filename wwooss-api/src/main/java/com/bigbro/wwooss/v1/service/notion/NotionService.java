package com.bigbro.wwooss.v1.service.notion;

import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResponse;

public interface NotionService {

    /**
     * get quiz in notion
     * @Nullable @param nextCursor
     * @return
     */
    QuizDBResponse getNotionQuizDB(String nextCursor);

    /**
     * notion status 컬럼 Done으로 변경
     * @param pageId
     */
    void updateStatusDone(String pageId);
}
