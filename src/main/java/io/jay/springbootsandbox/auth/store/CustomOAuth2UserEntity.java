package io.jay.springbootsandbox.auth.store;

import io.jay.springbootsandbox.auth.domain.CustomOAuth2User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER")
public class CustomOAuth2UserEntity extends BaseTimeEntity {

    @Id
    private String id;
    private String provider;

    private String uniqueId;

    public CustomOAuth2UserEntity(CustomOAuth2User user) {
        this.id = user.getId();
        this.provider = user.getProvider();
        this.uniqueId = user.getUniqueId() != null ? user.getUniqueId() : null;
    }
}
