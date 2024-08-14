package com.bigbro.wwooss.v1.api.quizHistory;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.quizHistory.QuizHistoryListResponse;
import com.bigbro.wwooss.v1.entity.quiz.QuizHistory;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.quizHistory.QuizHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/quiz-history")
@RequiredArgsConstructor
public class QuizHistoryApi {

    private final QuizHistoryService quizHistoryService;

    @GetMapping
    public ResponseEntity<WwoossResponse<QuizHistoryListResponse>> getQuizHistory(Pageable pageable,
                                                                                  UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(quizHistoryService.getQuizHistoryList(userCredential.getUserId(), pageable));
    }
}
