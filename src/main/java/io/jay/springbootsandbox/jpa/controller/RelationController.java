package io.jay.springbootsandbox.jpa.controller;

import io.jay.springbootsandbox.jpa.repository.Member;
import io.jay.springbootsandbox.jpa.repository.Team;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class RelationController {

    private final EntityManager entityManager;

    public RelationController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/test")
    @Transactional
    public void insertTeamAndMembers() {
        Team team = Team.builder()
                .id("1")
                .name("team one")
                .build();
        entityManager.persist(team);

        Member firstMember = Member.builder()
                .id("m1")
                .username("John")
                .team(team)
                .build();
        entityManager.persist(firstMember);
        Member secondMember = Member.builder()
                .id("m2")
                .username("Sam")
                .team(team)
                .build();
        entityManager.persist(secondMember);
    }

    @GetMapping("/members")
    @Transactional
    public List<MemberDto> getTeamMembers() {
        List<MemberDto> members = new ArrayList<>();
        Team team = entityManager.find(Team.class, "1");
        for (Member member : team.getMembers()) {
            MemberDto m = new MemberDto();
            m.setId(member.getId());
            m.setUsername(member.getUsername());
            m.setTeamId(member.getTeam().getId());
            m.setTeamName(member.getTeam().getName());
            members.add(m);
        }
        return members;
    }

    @GetMapping("/teams")
    @Transactional
    public Set<TeamDto> getTeams() {
        Set<TeamDto> team = new HashSet<>();
        Member firstMember = entityManager.find(Member.class, "m1");
        TeamDto firstTeam = new TeamDto();
        firstTeam.setId(firstMember.getTeam().getId());
        firstTeam.setName(firstMember.getTeam().getName());
        team.add(firstTeam);

        Member secondMember = entityManager.find(Member.class, "m2");
        TeamDto secondTeam = new TeamDto();
        secondTeam.setId(secondMember.getTeam().getId());
        secondTeam.setName(secondMember.getTeam().getName());
        team.add(secondTeam);
        return team;
    }
}
