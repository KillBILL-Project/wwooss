package com.bigbro.wwooss.v1.entity.report;

import com.bigbro.wwooss.v1.dto.WeeklyTrashByCategory;
import com.bigbro.wwooss.v1.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weekly_report")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyReport extends BaseEntity {

    @Id
    @Column(name = "weekly_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weeklyReportId;

    @Comment("출석 요일 [0 ~ 6 / 월 ~ 일]")
    @Type(type = "json")
    @Column(name = "attendance_records", columnDefinition = "json")
    @Builder.Default
    private List<Integer> attendanceRecords = new ArrayList<>();


    @Comment("주간 카테고리별 쓰레기 ")
    @Column(name = "weekly_trash_count_by_category", columnDefinition = "json")
    @Type(type = "json")
    @Builder.Default
    private List<WeeklyTrashByCategory> weeklyTrashCountByCategoryList = new ArrayList<>();

    @Comment("주간 탄소배출량")
    @Column(name = "weekly_carbon_emission")
    private Double weeklyCarbonEmission;

    @Comment("주간 환급금")
    @Column(name = "weekly_refund")
    private Double weeklyRefund;

    @Comment("금주 버린 쓰레기 수")
    @Column(name = "weekly_trash_count")
    private Long weeklyTrashCount;

    @Comment("몇주차")
    @Column(name = "week_number")
    private Long weekNumber;

    @Comment("전주대비 탄소배출량 증감 / Week On Week")
    @Column(name = "wow_carbon_emission")
    private Double wowCarbonEmission;

    @Comment("전주대비 환불 증감 / Week On Week")
    @Column(name = "wow_refund")
    private Long wowRefund;

    @Comment("전주대비 버린 쓰레기수 즘감 / Week On Week")
    @Column(name = "wow_trash_count")
    private Long wowTrashCount;

}