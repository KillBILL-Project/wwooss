package com.bigbro.wwooss.v1.entity.teamUser;

import com.bigbro.wwooss.v1.entity.team.Team;
import com.bigbro.wwooss.v1.entity.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "team_user")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamUser {

    @Id
    @Column(name = "team_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
