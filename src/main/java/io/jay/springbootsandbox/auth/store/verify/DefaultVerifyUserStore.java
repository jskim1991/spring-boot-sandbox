package io.jay.springbootsandbox.auth.store.verify;

import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class DefaultVerifyUserStore implements VerifyUserStore {

    private final VerifyUserRepository repository;

    public DefaultVerifyUserStore(VerifyUserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void addOrUpdateNewUserToVerify(VerifyUser user) {
        Optional<VerifyUser> result = repository.findById(user.getId());
        if (result.isPresent()) {
            VerifyUser verifyUser = result.get();
            verifyUser.setUniqueCode(user.getUniqueCode());
        } else {
            repository.save(user);
        }
    }

    @Override
    public String getUniqueCode(String userId) {
        Optional<VerifyUser> user = repository.findById(userId);
        return user.get().getUniqueCode();
    }
}
