package com.bigbro.wwooss.v1.api.quiz;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.response.quiz.QuizResponse;
import com.bigbro.wwooss.v1.enumType.QuizStatusType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/quiz")
@RequiredArgsConstructor
public class QuizApi {

    private final QuizService quizService;


    @GetMapping("/{quiz-id}")
    public ResponseEntity<WwoossResponse<QuizResponse>> findQuiz(@PathVariable(name = "quiz-id") Long quizId) {
        return WwoossResponseUtil.responseOkAddData(quizService.findQuiz(quizId));
    }

    @PatchMapping("/{quiz-id}")
    public ResponseEntity<WwoossResponse<QuizStatusType>> solveQuiz(@PathVariable(name = "quiz-id") Long quizId,
                                                                    @RequestParam(name = "answer") String answer,
                                                                    UserCredential userCredential) {
        return WwoossResponseUtil.responseOkAddData(quizService.solveQuiz(userCredential.getUserId(), quizId, answer));
    }

}
