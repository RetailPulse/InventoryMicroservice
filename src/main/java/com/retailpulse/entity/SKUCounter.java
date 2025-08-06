package com.retailpulse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class SKUCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; // To differentiate counters if needed. e.g. "product"

    private Long counter;

    public SKUCounter(String name, Long counter) {
        this.name = name;
        this.counter = counter;
    }
}
