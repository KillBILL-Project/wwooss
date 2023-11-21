package com.bigbro.wwooss.v1.job;

import static com.bigbro.wwooss.v1.enumType.NotificationTemplateCode.WWOOSS_ALARM;
import static java.time.LocalDateTime.parse;

import com.bigbro.wwooss.v1.dto.request.notification.NotificationSendRequest;
import com.bigbro.wwooss.v1.entity.alarm.Alarm;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.repository.alarm.AlarmRepository;
import com.bigbro.wwooss.v1.service.notification.NotificationService;
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
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AlarmTasklet implements Tasklet, StepExecutionListener {

    private List<Alarm> alarmList;

    private int dayOfWeek;

    private final AlarmRepository alarmRepository;

    private final NotificationService notificationService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String date = stepExecution.getJobParameters().getParameters().get("date").toString();
        LocalDateTime parseDate = parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        dayOfWeek = parseDate.getDayOfWeek().getValue();

        alarmList = alarmRepository.findAlarmAtTime(parseDate.getHour(), parseDate.getMinute(),
                parseDate.getDayOfWeek().getValue());
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<Alarm> sendAlarmList = alarmList.stream()
                .filter((alarm) -> alarm.getDayOfWeekList().contains(dayOfWeek)).toList();

        if (!sendAlarmList.isEmpty()) {
            notificationService.sendMany(buildRequestNotification(sendAlarmList));
        }
        return RepeatStatus.FINISHED;
    }

    private NotificationSendRequest buildRequestNotification(List<Alarm> alarmList) {
        List<User> targets = alarmList.stream().map(Alarm::getUser).toList();
        return NotificationSendRequest.of(targets, WWOOSS_ALARM);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
