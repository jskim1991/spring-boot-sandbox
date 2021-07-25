package io.jay.springbootsandbox.auth;

import lombok.Data;

@Data
public class VerificationBody {

    private String userId;
    private String verificationCode;
}
