package io.jay.springbootsandbox.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenProvider implements TokenProvider {
    private final long MINUTES = 60 * 1000L;
    private final long ACCESS_TOKEN_VALID_TIME = 10 * MINUTES;
    private final long REFRESH_TOKEN_VALID_TIME = 30 * MINUTES;
    private JwtSecretKey secretKey;

    public JwtTokenProvider(JwtSecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String createAccessToken(String email) {
        return createToken(email, ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(String email, List<String> roles) {
        return createToken(email, REFRESH_TOKEN_VALID_TIME);
    }

    private String createToken(String id, long validTime) {
        Claims claims = Jwts.claims().setSubject(id);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey.getSecretKeyAsBytes())
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "X-AUTH-TOKEN");
        return cookie.getValue();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getSecretKeyAsBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUserId(String token) {
        String id = Jwts.parser()
                .setSigningKey(secretKey.getSecretKeyAsBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return id;
    }
}
