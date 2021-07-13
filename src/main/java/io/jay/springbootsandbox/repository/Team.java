package io.jay.springbootsandbox.repository;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    /* using targetEntity
    @OneToMany(targetEntity = Member.class)
    private List<Member> members;
    */

    @Builder
    public Team(String id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
    }

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
}
