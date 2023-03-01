package org.example.crypto.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;
    private String email;

    private String secretKey;

    private Role role;

    @Builder
    public UserEntity(String username, String email, String secretKey, Role role) {
        this.username = username;
        this.email = email;
        this.secretKey = secretKey;
        this.role = role;
    }
}
