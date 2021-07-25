package io.jay.springbootsandbox.auth.service;

import io.jay.springbootsandbox.auth.domain.CustomOAuth2User;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserEntity;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomOAuth2UserStore store;

    public CustomUserDetailsService(CustomOAuth2UserStore store) {
        this.store = store;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomOAuth2UserEntity user = store.retrieve(username);
        return CustomOAuth2User.create(user);
    }
}
