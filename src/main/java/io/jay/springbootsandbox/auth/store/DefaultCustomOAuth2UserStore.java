package io.jay.springbootsandbox.auth.store;

import io.jay.springbootsandbox.auth.domain.CustomOAuth2User;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@EnableJpaAuditing
public class DefaultCustomOAuth2UserStore implements CustomOAuth2UserStore {

    private final CustomOAuth2UserRepository repository;

    public DefaultCustomOAuth2UserStore(CustomOAuth2UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomOAuth2UserEntity insert(CustomOAuth2User user) {
        CustomOAuth2UserEntity savedEntity = repository.save(new CustomOAuth2UserEntity(user));
        return savedEntity;
    }

    @Override
    public CustomOAuth2UserEntity retrieve(String id) {
        return repository.findById(id).get();
    }

    @Override
    public CustomOAuth2UserEntity retrieveByIdAndProvider(String id, String provider) {
        Optional<CustomOAuth2UserEntity> result = repository.findByIdAndProvider(id, provider);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public CustomOAuth2UserEntity updateUniqueId(String id, String uniqueId) {
        Optional<CustomOAuth2UserEntity> byId = repository.findById(id);
        CustomOAuth2UserEntity entity = byId.get();
        entity.setUniqueId(uniqueId);
        return entity;
    }
}
