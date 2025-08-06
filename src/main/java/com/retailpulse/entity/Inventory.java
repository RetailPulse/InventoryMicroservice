package com.retailpulse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long businessEntityId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double totalCostPrice = 0.0;
}
