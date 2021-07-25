package io.jay.springbootsandbox.auth.store;

import io.jay.springbootsandbox.auth.domain.CustomOAuth2User;

public interface CustomOAuth2UserStore {

    CustomOAuth2UserEntity insert(CustomOAuth2User user);

    CustomOAuth2UserEntity retrieve(String id);

    CustomOAuth2UserEntity retrieveByIdAndProvider(String id, String provider);

    CustomOAuth2UserEntity updateUniqueId(String id, String uniqueId);
}
