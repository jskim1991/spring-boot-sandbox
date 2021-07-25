package io.jay.springbootsandbox.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthLoginResponse {
    private String userId;
    private String token;

    @Builder.Default
    private boolean requireSignup = false;
}
