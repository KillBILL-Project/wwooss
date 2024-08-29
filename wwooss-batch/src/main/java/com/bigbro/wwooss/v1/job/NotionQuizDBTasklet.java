package com.bigbro.wwooss.v1.job;

import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResponse;
import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResult;
import com.bigbro.wwooss.v1.dto.response.notion.QuizDBResultProperty;
import com.bigbro.wwooss.v1.entity.quiz.Quiz;
import com.bigbro.wwooss.v1.service.quizOptions.QuizOptionsService;
import com.bigbro.wwooss.v1.service.notion.NotionService;
import com.bigbro.wwooss.v1.service.quiz.QuizService;
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

import java.util.List;


@RequiredArgsConstructor
@Component
@Slf4j
public class NotionQuizDBTasklet implements Tasklet, StepExecutionListener {

    private final NotionService notionService;

    private final QuizService quizService;

    private final QuizOptionsService quizOptionsService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String nextCursor = null;
        boolean hasMore = true;
        while (hasMore) {
            QuizDBResponse notionQuizDB = notionService.getNotionQuizDB(nextCursor);
            List<QuizDBResult> results = notionQuizDB.getResults();

            for (int i = 0; i < results.size(); i++) {
                QuizDBResult quizDBResult = results.get(i);
                QuizDBResultProperty properties = quizDBResult.getProperties();

                // save quiz
                Quiz quiz = quizService.saveQuiz(properties);
                // save option
                quizOptionsService.saveOptions(quiz, properties);
                // update notion status
                notionService.updateStatusDone(quizDBResult.getId());
            }

            hasMore = notionQuizDB.getHas_more();
            nextCursor = notionQuizDB.getNext_cursor();
        }

        return RepeatStatus.FINISHED;
    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
