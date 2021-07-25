package io.jay.springbootsandbox.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class KakaoUser extends CustomOAuth2User {

    public KakaoUser() {
        super();
    }

    @Builder
    public KakaoUser(String id, String provider, Set<GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(id, provider, authorities, attributes);
    }
}
