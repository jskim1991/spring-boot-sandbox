package io.jay.springbootsandbox.auth.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomOAuth2UserRepository extends JpaRepository<CustomOAuth2UserEntity, String> {
    Optional<CustomOAuth2UserEntity> findByIdAndProvider(String id, String provider);
}
