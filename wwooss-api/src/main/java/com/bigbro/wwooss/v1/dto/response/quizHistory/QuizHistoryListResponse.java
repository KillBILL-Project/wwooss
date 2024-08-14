package com.bigbro.wwooss.v1.dto.response.quizHistory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class QuizHistoryListResponse {
    private Boolean hasNext;


    private List<QuizHistoryInfo> quizHistoryInfoList;

    public static QuizHistoryListResponse of(Boolean hasNext, List<QuizHistoryInfo> quizHistoryInfoList) {
        return QuizHistoryListResponse.builder()
                .hasNext(hasNext)
                .quizHistoryInfoList(quizHistoryInfoList)
                .build();
    }
}
