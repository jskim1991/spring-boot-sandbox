package io.jay.springbootsandbox.auth;

import io.jay.springbootsandbox.auth.domain.CustomOAuth2User;
import io.jay.springbootsandbox.auth.domain.KakaoProfile;
import io.jay.springbootsandbox.auth.domain.KakaoUser;
import io.jay.springbootsandbox.auth.domain.OAuthToken;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserEntity;
import io.jay.springbootsandbox.auth.store.CustomOAuth2UserStore;
import io.jay.springbootsandbox.auth.store.verify.VerifyUser;
import io.jay.springbootsandbox.auth.store.verify.VerifyUserStore;
import io.jay.springbootsandbox.auth.token.TokenProvider;
import io.jay.springbootsandbox.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final CustomOAuth2UserStore store;
    private final VerifyUserStore tempStore;
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;

    @GetMapping("/login")
    public void get() {
        System.out.println("login");
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal CustomOAuth2User user, HttpServletRequest request) {
        return "welcome " + user.getId();
    }

    @GetMapping("/hello")
    public String hello(HttpServletResponse response) {
        response.addCookie(new Cookie("color", "hello"));
        response.addHeader("testHeader", "aaa");
        return "hello";
    }

    @GetMapping("/bye")
    public String hello2(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(new Cookie("color2", "hello2"));
        response.addHeader("testHeader2", "bbb");
        return "hello";

    }

    @GetMapping("/")
    public void redirect(HttpServletResponse response) {
        response.setHeader("Location", "https://naver.com");
        response.setStatus(302);
    }

    @PostMapping("/auth/sms")
    public String sendSms(HttpServletRequest request, @RequestBody VerificationBody verificationBody) {
//        String userId = WebUtils.getCookie(request, "userId").getValue();
        String uniqueCode = UUID.randomUUID().toString();
        VerifyUser verifyUser = new VerifyUser();
        verifyUser.setId(verificationBody.getUserId());
        verifyUser.setUniqueCode(uniqueCode);
        tempStore.addOrUpdateNewUserToVerify(verifyUser);

        return "success";
    }

    @PostMapping("/auth/verify")
    public String verify(HttpServletRequest request, HttpServletResponse response, @RequestBody VerificationBody verificationBody) {
//        String userId = WebUtils.getCookie(request, "userId").getValue();
        String userId = verificationBody.getUserId();
        String uniqueCode = tempStore.getUniqueCode(userId);

        store.updateUniqueId(userId, uniqueCode);
//        Cookie cookieToDelete = new Cookie("userId", "");
//        cookieToDelete.setPath("/");
//        cookieToDelete.setMaxAge(0);
//        response.addCookie(cookieToDelete);

        String accessToken = tokenProvider.createAccessToken(userId);
        System.out.println("TOKEN COOKIE " + accessToken);
//        Cookie tokenCookie = new Cookie("access_token", accessToken);
//        tokenCookie.setPath("/");
//        response.addCookie(tokenCookie);
        return accessToken;
    }

    @GetMapping("/oauth2/callback/kakao")
    public OAuthLoginResponse kakaoLogin(@RequestParam String code) {

        OAuthToken oAuthToken = getOAuth2AccessToken(code);

        System.out.println(oAuthToken.getAccess_token());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest, String.class);

        KakaoProfile kakaoProfile = JsonUtil.readValue(response.getBody(), KakaoProfile.class);
        System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
        System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());
        System.out.println("블로그서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("블로그서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());

        KakaoUser kakaoUser = KakaoUser.builder()
                .id(String.valueOf(kakaoProfile.getId()))
                .provider("kakao")
                .build();

        String userId = kakaoUser.getId();
        CustomOAuth2UserEntity entity = store.retrieveByIdAndProvider(userId, "kakao");
        if (entity != null) {
            if (entity.getUniqueId() != null) {
                String accessToken = tokenProvider.createAccessToken(userId);
                return OAuthLoginResponse.builder()
                        .userId(userId)
                        .token(accessToken)
                        .requireSignup(false)
                        .build();
            }
        } else {
            store.insert(kakaoUser);
            return OAuthLoginResponse.builder()
                    .requireSignup(true)
                    .userId(userId)
                    .build();
        }

        return OAuthLoginResponse.builder().build();
    }

    private OAuthToken getOAuth2AccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", "b6bb768063205b3d090001f316b906c4");
        parameters.add("redirect_uri", "http://localhost:3000/oauth/callback/kakao");
        parameters.add("client_secret", "IjbAMP3VIBzATS99kM2qBdNkDHrNeqli");
        parameters.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        OAuthToken oAuthToken = JsonUtil.readValue(response.getBody(), OAuthToken.class);
        return oAuthToken;
    }
}
