package io.jay.springbootsandbox.auth.store.verify;

import io.jay.springbootsandbox.auth.store.BaseTimeEntity;
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
@Table(name = "verify")
public class VerifyUser extends BaseTimeEntity {
    @Id
    private String id;
    private String uniqueCode;
}
