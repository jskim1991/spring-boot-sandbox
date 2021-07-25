package io.jay.springbootsandbox.jpa.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private String id;
    private String username;
    private String teamId;
    private String teamName;
}
