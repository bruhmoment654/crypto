package org.example.crypto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Wallet {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    private String currency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    private String name;
    private Double sum;

    @Builder
    public Wallet(String currency, UserEntity user, String name, Double sum) {
        this.currency = currency;
        this.user = user;
        this.name = name;
        this.sum = sum;
    }
}
