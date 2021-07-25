package io.jay.springbootsandbox.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class NaverUser extends CustomOAuth2User {
    private String nickname;
    private String email;
    private String mobile;

    @Builder
    public NaverUser(String id, String provider, Set<GrantedAuthority> authorities, Map<String, Object> attributes,
                     String nickname, String email, String mobile) {
        super(id, provider, authorities, attributes);
        this.nickname = nickname;
        this.email = email;
        this.mobile = mobile;
    }
}
