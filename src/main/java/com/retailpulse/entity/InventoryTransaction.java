package com.retailpulse.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Acts like a transactionId

    @Column(nullable = false)
    private Long productId; // ProductId - Unique Identifier for Product

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double costPricePerUnit;
    /*
     * Supplier - External
     * Central Inventory
     * Shop(s)
     */
    @Column(nullable = false)
    private Long source; // BusinessEntityId - InventoryService coming from
    @Column(nullable = false)
    private Long destination; // BusinessEntityId - InventoryService going to

    @Column(nullable = false)
    @CreationTimestamp
    // Automatically set when the entity is persisted
    private Instant insertedAt; // Using Instant to make sure follow application.yml config on timezone

    // @ManyToOne
    // @JoinColumn(name = "product_id", referencedColumnName = "id")
    // private Product product;
}
