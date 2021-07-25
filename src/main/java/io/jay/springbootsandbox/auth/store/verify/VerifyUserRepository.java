package io.jay.springbootsandbox.auth.store.verify;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyUserRepository extends JpaRepository<VerifyUser, String> {
}
