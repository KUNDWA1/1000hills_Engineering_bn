package com.thousandhills.backend.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String sku;
    
    private String title;
    private Double price;
    private String currency;
    private String category; 
    
    @Column(name = "image_url")
    private String imageUrl; 
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    
    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ProductSpecification> specifications = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private VendorProfile vendor;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    // ==========================================
    // 1. CONSTRUCTORS
    // ==========================================
    
    // No-Args Constructor (Required by JPA)
    public Product() {
    }

    // All-Args Constructor
    public Product(Long id, String sku, String title, Double price, String currency, String category, 
                   String imageUrl, Integer stockQuantity, Boolean isFeatured, String description, 
                   Set<ProductImage> images, Set<ProductSpecification> specifications, VendorProfile vendor, Boolean isApproved) {
        this.id = id;
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.category = category;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.isFeatured = isFeatured;
        this.description = description;
        this.images = images;
        this.specifications = specifications;
        this.vendor = vendor;
        this.isApproved = isApproved;
    }

    // ==========================================
    // 2. GETTERS AND SETTERS (Explicitly Defined)
    // ==========================================
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // This explicitly defines getCategory() so ProductService can see it natively!
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public Set<ProductSpecification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Set<ProductSpecification> specifications) {
        this.specifications = specifications;
    }

    public VendorProfile getVendor() {
        return vendor;
    }

    public void setVendor(VendorProfile vendor) {
        this.vendor = vendor;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}