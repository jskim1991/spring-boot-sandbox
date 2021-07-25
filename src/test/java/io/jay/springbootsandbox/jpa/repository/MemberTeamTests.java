package io.jay.springbootsandbox.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class MemberTeamTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_member_team_relation() {
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

        /* 연관관계 주인이 아닌쪽은 별도 추가 불필요
        team.getMembers().add(firstMember);
        team.getMembers().add(secondMember);
         */
        entityManager.flush();
        entityManager.clear();


        Member returnedMember = entityManager.find(Member.class, "m2");
        assertThat(returnedMember.getTeam().getId(), equalTo("1"));
        assertThat(returnedMember.getTeam().getName(), equalTo("team one"));


        Team returnedTeam = entityManager.find(Team.class, "1");
        assertThat(returnedTeam.getMembers().size(), equalTo(2));
        assertThat(returnedTeam.getMembers().get(0).getId(), equalTo("m1"));
        assertThat(returnedTeam.getMembers().get(1).getId(), equalTo("m2"));
    }

}

/* Using targetEntity
create table member (member_id varchar(255) not null, username varchar(255), team_id varchar(255), primary key (member_id))
create table team (team_id varchar(255) not null, name varchar(255), primary key (team_id))
create table team_members (team_team_id varchar(255) not null, members_member_id varchar(255) not null)
alter table team_members add constraint UK_qakwdx6c7b4hmfj8545dj90fg unique (members_member_id)
alter table member add constraint FKcjte2jn9pvo9ud2hyfgwcja0k foreign key (team_id) references team
alter table team_members add constraint FKjn19iu2oeu7hrdwgksruki0mn foreign key (members_member_id) references member
alter table team_members add constraint FK2ym1sj502875uhxqysx3xlmbb foreign key (team_team_id) references team

select
    member0_.member_id as member_i1_0_0_,
    member0_.team_id as team_id3_0_0_,
    member0_.username as username2_0_0_,
    team1_.team_id as team_id1_1_1_,
    team1_.name as name2_1_1_
from member member0_
left outer join team team1_
on member0_.team_id=team1_.team_id
where member0_.member_id=?
*/

/* Using mappedBy - 외래 키 하나로 두 테이블의 연관관계를 관리함
create table member (member_id varchar(255) not null, username varchar(255), team_id varchar(255), primary key (member_id))
create table team (team_id varchar(255) not null, name varchar(255), primary key (team_id))
alter table member add constraint FKcjte2jn9pvo9ud2hyfgwcja0k foreign key (team_id) references team

select
    member0_.member_id as member_i1_0_0_,
    member0_.team_id as team_id3_0_0_,
    member0_.username as username2_0_0_,
    team1_.team_id as team_id1_1_1_,
    team1_.name as name2_1_1_
from member member0_
left outer join team team1_
on member0_.team_id=team1_.team_id
where member0_.member_id=?
*/
