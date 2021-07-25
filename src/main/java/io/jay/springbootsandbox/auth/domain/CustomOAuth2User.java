package io.jay.springbootsandbox.auth.domain;

import io.jay.springbootsandbox.auth.store.CustomOAuth2UserEntity;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserStore;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Setter
public class CustomOAuth2User implements OAuth2User, UserDetails {

    private String id;
    private String provider;

    private Set<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    private String uniqueId;

    public CustomOAuth2User() {
    }

    public CustomOAuth2User(String id, String provider, Set<GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.id = id;
        this.provider = provider;
        this.authorities = authorities;
        this.attributes = attributes;
//        this.nameAttributeKey = nameAttributeKey;
    }

    public static CustomOAuth2User create(CustomOAuth2UserEntity entity) {
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        CustomOAuth2User user = new CustomOAuth2User();
        user.setId(entity.getId());
        user.setProvider(entity.getProvider());
        user.setUniqueId(entity.getUniqueId());
        user.setAuthorities(authorities);
        return user;
    }

    public static CustomOAuth2User create(CustomOAuth2UserEntity entity, Map<String, Object> attributes) {
        CustomOAuth2User user = CustomOAuth2User.create(entity);
        user.setAttributes(attributes);
        return user;
    }

    public String getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return id;
    }
}
