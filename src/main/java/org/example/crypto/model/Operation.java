package org.example.crypto.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Operation {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = "DD.MM.YYYY")
    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String currency;

    private String wallet;

    private Double sum;
    private String operationType;

    @Builder
    public Operation(LocalDate operationDate, UserEntity user, String currency, String wallet, Double sum, String operationType) {
        this.transactionDate = operationDate;
        this.user = user;
        this.currency = currency;
        this.wallet = wallet;
        this.sum = sum;
        this.operationType = operationType;
    }
}
