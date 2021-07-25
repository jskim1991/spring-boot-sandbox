package io.jay.springbootsandbox.auth;

import io.jay.springbootsandbox.auth.domain.CustomOAuth2User;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserEntity;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserStore;
import io.jay.springbootsandbox.auth.token.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final CustomOAuth2UserStore store;
    private final TokenProvider tokenProvider;

    public CustomAuthenticationSuccessHandler(CustomOAuth2UserStore store, TokenProvider tokenProvider) {
        this.store = store;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
            String registrationId = authenticationToken.getAuthorizedClientRegistrationId();
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            String userId = principal.getId();
            HttpSession session = request.getSession(false);
            session.setAttribute("userId", userId);

            CustomOAuth2UserEntity entity = store.retrieveByIdAndProvider(userId, registrationId);
            if (entity != null) {
                if (entity.getUniqueId() != null) {
                    String accessToken = tokenProvider.createAccessToken(userId);
                    Cookie cookie = new Cookie("access_token", accessToken);
                    System.out.println("NEW TOKEN COOKIE " + accessToken);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    response.sendRedirect("http://localhost:3000/welcome");
                }
            } else {
                store.insert(principal);
                Cookie cookie = new Cookie("userId", userId);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        if (!response.isCommitted()) {
            response.sendRedirect("http://localhost:3000/signup");
        }
    }
}
