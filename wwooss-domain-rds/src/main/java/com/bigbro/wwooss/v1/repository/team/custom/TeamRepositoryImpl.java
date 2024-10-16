package com.bigbro.wwooss.v1.repository.team.custom;

import com.bigbro.wwooss.v1.entity.team.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.bigbro.wwooss.v1.entity.team.QTeam.team;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Team findCompanyTeam(String teamName, String address, String businessNo, String email) {
        return jpaQueryFactory.selectFrom(team)
                .where(team.businessNo.eq(businessNo)
                        .and(team.teamName.eq(teamName)
                                .and(team.address.eq(address))
                                .and(team.email.eq(email))))
                .fetchOne();
    }

    @Override
    public Team findSchoolTeam(String teamName, String address, String email) {
        return jpaQueryFactory.selectFrom(team)
                .where((team.teamName.eq(teamName)
                                .and(team.address.eq(address))
                                .and(team.email.eq(email))))
                .fetchOne();
    }

    @Override
    public Team findCrewTeam(String teamName, String email) {
        return jpaQueryFactory.selectFrom(team)
                .where((team.teamName.eq(teamName)
                        .and(team.email.eq(email))))
                .fetchOne();
    }
}
