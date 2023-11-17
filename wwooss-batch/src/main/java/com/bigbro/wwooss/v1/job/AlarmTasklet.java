package com.bigbro.wwooss.v1.job;

import static java.time.LocalDateTime.parse;

import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.repository.alarm.AlarmRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@RequiredArgsConstructor
public class AlarmTasklet implements Tasklet, StepExecutionListener {

    private List<Alarm> alarmList;

    private final AlarmRepository alarmRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String date = stepExecution.getJobParameters().getParameters().get("date").toString();
        LocalDateTime parseDate = parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        alarmList = alarmRepository.findAlarmAtTime(parseDate.getHour(), parseDate.getMinute(),
                parseDate.getDayOfWeek().getValue());
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
