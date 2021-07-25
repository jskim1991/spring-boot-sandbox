package io.jay.springbootsandbox.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.jay.springbootsandbox.auth.domain.KakaoUser;
import io.jay.springbootsandbox.auth.domain.NaverUser;
import io.jay.springbootsandbox.util.JsonUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static io.jay.springbootsandbox.auth.domain.OAuth2Provider.KAKAO;
import static io.jay.springbootsandbox.auth.domain.OAuth2Provider.NAVER;

@Service
public class CustomOAuth2UserService implements OAuth2UserService {

    private final RestOperations restOperations;

    public CustomOAuth2UserService(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String userNameAttributeName = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        RequestEntity<?> request = new OAuth2UserRequestEntityConverter().convert(userRequest);
        ResponseEntity<Map<String, Object>> response = getResponse(userRequest, request);
        Map<String, Object> attributes = response.getBody();
        Set<GrantedAuthority> role = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        if (KAKAO.getClientName().equals(clientRegistration.getRegistrationId())) {
            KakaoUser kakaoUser = KakaoUser.builder()
                    .id(attributes.get(userNameAttributeName).toString())
                    .provider(clientRegistration.getRegistrationId())
                    .attributes(attributes)
                    .authorities(role)
                    .build();
            return kakaoUser;

        } else if (NAVER.getClientName().equals(clientRegistration.getRegistrationId())) {
            JsonNode jsonNode = JsonUtil.readTree(JsonUtil.toJson(attributes.get(userNameAttributeName)));
            return NaverUser.builder()
                    .id(jsonNode.get("id").textValue())
                    .nickname(jsonNode.get("nickname").textValue())
                    .email(jsonNode.get("email").textValue())
                    .mobile(jsonNode.get("mobile").textValue())
                    .provider(clientRegistration.getRegistrationId())
                    .attributes(attributes)
                    .authorities(role)
                    .build();
        } else {
            throw new IllegalArgumentException("unsupported login registration");
        }
    }

    private ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
        return this.restOperations.exchange(request, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }
}
