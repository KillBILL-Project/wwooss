package com.bigbro.wwooss.v1.job;

import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.service.quiz.QuizService;
import com.bigbro.wwooss.v1.service.quizHistory.QuizHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public class DailyCreateQuizTasklet implements Tasklet, StepExecutionListener {


    private final QuizService quizService;

    private final QuizHistoryService quizHistoryService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Quiz newQuiz = quizService.findNewQuiz();
        if (Objects.nonNull(newQuiz)) {
            quizHistoryService.createQuizHistoryAllUser(newQuiz);
            newQuiz.updateOpenStatus(true);
        }
        return RepeatStatus.FINISHED;
    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
