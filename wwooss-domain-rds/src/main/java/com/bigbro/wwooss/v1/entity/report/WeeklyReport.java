package com.bigbro.wwooss.v1.entity.report;

import com.bigbro.wwooss.v1.converter.IntegerListConverter;
import com.bigbro.wwooss.v1.converter.WeeklyTrashListConverter;
import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.dto.WowReport;
import com.bigbro.wwooss.v1.entity.user.User;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weekly_report")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyReport {

    @Id
    @Column(name = "weekly_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weeklyReportId;

    @Comment("출석 요일 [1 ~ 7 / 월 ~ 일]")
    @Column(name = "attendance_record")
    @Convert(converter = IntegerListConverter.class)
    @Builder.Default
    private List<Integer> attendanceRecord = new ArrayList<>();

    @Comment("주간 카테고리별 쓰레기 ")
    @Column(name = "weekly_trash_count_by_category")
    @Convert(converter = WeeklyTrashListConverter.class)
    @Builder.Default
    private List<WeeklyTrashByCategory> weeklyTrashCountByCategoryList = new ArrayList<>();

    @Comment("주간 탄소배출량")
    @Column(name = "weekly_carbon_emission")
    private Double weeklyCarbonEmission;

    @Comment("주간 환급금")
    @Column(name = "weekly_refund")
    private Long weeklyRefund;

    @Comment("금주 버린 쓰레기 수")
    @Column(name = "weekly_trash_count")
    private Long weeklyTrashCount;

    @Comment("전주대비 탄소배출량 증감 / Week On Week")
    @Column(name = "wow_carbon_emission")
    private Double wowCarbonEmission;

    @Comment("전주대비 환불 증감 / Week On Week")
    @Column(name = "wow_refund")
    private Long wowRefund;

    @Comment("전주대비 버린 쓰레기수 즘감 / Week On Week")
    @Column(name = "wow_trash_count")
    private Long wowTrashCount;

    @Comment("주간 시작 날짜")
    private LocalDateTime weeklyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static WeeklyReport of(List<Integer> attendanceRecord,
            List<WeeklyTrashByCategory> weeklyTrashCountByCategoryList,
            Double weeklyCarbonEmission,
            Long weeklyRefund,
            Long weeklyTrashCount,
            WowReport wowReport,
            LocalDateTime weeklyDate,
            User user) {
        return WeeklyReport.builder()
                .attendanceRecord(attendanceRecord)
                .weeklyTrashCountByCategoryList(weeklyTrashCountByCategoryList)
                .weeklyCarbonEmission(weeklyCarbonEmission)
                .weeklyRefund(weeklyRefund)
                .weeklyTrashCount(weeklyTrashCount)
                .wowCarbonEmission(wowReport.getWowCarbonEmission())
                .wowRefund(wowReport.getWowRefund())
                .wowTrashCount(wowReport.getWowTrashCount())
                .weeklyDate(weeklyDate)
                .user(user)
                .build();
    }

}
