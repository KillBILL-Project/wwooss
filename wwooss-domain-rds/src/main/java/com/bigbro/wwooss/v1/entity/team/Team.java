package com.bigbro.wwooss.v1.entity.team;

import com.bigbro.wwooss.v1.entity.teamUser.TeamUser;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Comment("팀 타입 - 기업 / 학교 / 크루 / 지역")
    @Column(name = "team_type")
    @Enumerated(value = EnumType.STRING)
    private TeamType teamType;

    @Comment("팀 이름")
    @Column(name = "team_name")
    private String teamName;

    @Comment("주소")
    @Column(name = "address")
    private String address;

    @Comment("email")
    @Column(name = "email")
    private String email;

    @Comment("사업자 등록번호")
    @Column(name = "business_no")
    private String businessNo;

    @Comment("상태 - 승인 / 거절 / 대기")
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

    @Comment("logo image")
    @Column(name = "logo_image_path")
    private String logoImagePath;

    @Comment("기업 코드")
    @Column(name = "company_code")
    private String companyCode;

    @Comment("생성자")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createBy;

    @Comment("사업자 등록증")
    @Column(name = "business_regit_image_path")
    private String businessRegitImagePath;

    @OneToMany(mappedBy = "team")
    private List<TeamUser> teamUsers;

}
