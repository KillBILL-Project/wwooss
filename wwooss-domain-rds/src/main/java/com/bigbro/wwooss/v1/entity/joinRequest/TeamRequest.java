package com.bigbro.wwooss.v1.entity.joinRequest;

import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import com.bigbro.wwooss.v1.enumType.TeamRequestType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "team_request")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRequest {

    @Id
    @Column(name = "team_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamRequestId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requestUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team receiveTeam;

    @Comment("상태 - 승인 / 거절 / 대기")
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

    @Comment("요청 타입 - 생성 / 합류")
    @Column(name = "team_request_type")
    @Enumerated(value = EnumType.STRING)
    private TeamRequestType teamRequestType;

    @Comment("거절 사유")
    @Column(name = "reject_reason")
    private String rejectReason;

}
