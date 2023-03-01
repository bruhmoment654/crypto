package org.example.crypto.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private String currency1;
    private String currency2;

    private Double course;

    public Course(String currency1, String currency2) {
        this.currency1 = currency1;
        this.currency2 = currency2;
    }
}
