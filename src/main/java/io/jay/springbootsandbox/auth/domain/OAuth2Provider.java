package io.jay.springbootsandbox.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    KAKAO("kakao"),
    NAVER("naver");

    private final String clientName;
}
