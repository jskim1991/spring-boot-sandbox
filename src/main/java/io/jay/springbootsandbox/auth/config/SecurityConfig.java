package io.jay.springbootsandbox.auth.config;

import io.jay.springbootsandbox.auth.CustomAuthenticationSuccessHandler;
import io.jay.springbootsandbox.auth.filter.JwtRequestFilter;
import io.jay.springbootsandbox.auth.service.CustomOAuth2UserService;
import io.jay.springbootsandbox.auth.token.JwtSecretKey;
import io.jay.springbootsandbox.auth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
                .disable()
//                .and()
                .headers()
                .addHeaderWriter(
                        new XFrameOptionsHeaderWriter(
                                new WhiteListedAllowFromStrategy(Arrays.asList("localhost"))    // 여기!
                        )
                )
                .frameOptions().sameOrigin()    // 여기도 추가!!
                .and()
//                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**",
                        "/auth/**",
                        "/bye/**", "/hello/**", "/session/**",
                        "/oauth2/callback/kakao/**"
                ).permitAll()
                .anyRequest().authenticated()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(customAuthenticationSuccessHandler)
        ;
        http.addFilterBefore(new JwtRequestFilter(userDetailsService, tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

}