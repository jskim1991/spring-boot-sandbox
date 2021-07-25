package io.jay.springbootsandbox.auth.store.verify;

public interface VerifyUserStore {
    void addOrUpdateNewUserToVerify(VerifyUser user);
    String getUniqueCode(String userId);
}
