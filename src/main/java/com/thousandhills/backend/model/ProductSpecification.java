package com.thousandhills.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "product_specifications")
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spec_key", nullable = false)
    private String specKey;  // e.g., "System", "Voltage", "Capacity"

    @Column(name = "spec_value", nullable = false)
    private String specValue; // e.g., "48V", "51.2V", "100Ah"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================

    // No-Args Constructor (Required by JPA)
    public void ProductImage() {
    }

    // All-Args Constructor
    public ProductSpecification(Long id, String specKey, String specValue, Product product) {
        this.id = id;
        this.specKey = specKey;
        this.specValue = specValue;
        this.product = product;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecKey() {
        return specKey;
    }

    public void setSpecKey(String specKey) {
        this.specKey = specKey;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}