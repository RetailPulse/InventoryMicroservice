package com.retailpulse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String description;
    private String category;
    private String subcategory;
    private String brand;
    private String origin;
    private String uom;
    private String vendorCode;
    private String barcode;
    @Column(nullable = false)
    private double rrp = 0.0; // Recommended Retail Price

    @Column(nullable = false)
    private boolean active = true;
}
