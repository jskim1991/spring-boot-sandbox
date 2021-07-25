package io.jay.springbootsandbox.auth.token;

import java.util.List;

public interface TokenProvider {
    String createAccessToken(String email);
    String createRefreshToken(String email, List<String> roles);
    boolean validateToken(String token);
    String getUserId(String token);
}
