package com.bigbro.wwooss.v1.entity.joinRequest;

import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.entity.user.User;
import com.bigbro.wwooss.v1.enumType.RequestStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "join_request")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {

    @Id
    @Column(name = "join_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinRequestId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Comment("상태 - 승인 / 거절 / 대기")
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

}
