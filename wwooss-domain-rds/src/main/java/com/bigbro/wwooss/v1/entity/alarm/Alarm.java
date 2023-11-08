package com.bigbro.wwooss.v1.entity.alarm;

import com.bigbro.wwooss.v1.converter.DayOfWeekConverter;
import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.DayOfWeek;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "alarm")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Alarm extends BaseEntity {

    @Id
    @Column(name = "alarm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @Comment("알람 발송 요일 리스트")
    @Convert(converter = DayOfWeekConverter.class)
    @Column(name = "day_of_week")
    private List<DayOfWeek> dayOfWeekList;

    @Comment("알람 발송 시간")
    @Column(name = "send_time")
    private Integer sendHour;

    @Comment("알람 발송 분")
    @Column(name = "send_minute")
    private Integer sendMinute;

    @Comment("알람 on/off")
    @Column(name = "is_on")
    @Builder.Default
    private Boolean isOn = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Alarm of(List<DayOfWeek> dayOfWeekList, int sendHour, int sendMinute, User user) {
        return Alarm.builder()
                .dayOfWeekList(dayOfWeekList)
                .sendHour(sendHour)
                .sendMinute(sendMinute)
                .user(user)
                .build();
    }

    public void updateAlarm(List<DayOfWeek> dayOfWeekList, int sendHour, int sendMinute) {
        this.dayOfWeekList = dayOfWeekList;
        this.sendHour = sendHour;
        this.sendMinute = sendMinute;
    }

    public void switchAlarm(boolean isOn) {
        this.isOn = isOn;
    }
}
