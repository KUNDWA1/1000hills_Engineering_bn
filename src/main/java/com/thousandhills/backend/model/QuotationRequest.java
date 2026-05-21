package com.thousandhills.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quotation_requests")
public class QuotationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_reference", unique = true, nullable = false)
    private String requestReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private CustomerProfile customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    @JsonBackReference
    private VendorProfile vendor;

    @OneToMany(mappedBy = "quotationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<QuotationItem> items = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotationStatus status = QuotationStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public QuotationRequest() {
    }

    public QuotationRequest(Long id, String requestReference, CustomerProfile customer, VendorProfile vendor, Set<QuotationItem> items, String description, QuotationStatus status, Instant createdAt) {
        this.id = id;
        this.requestReference = requestReference;
        this.customer = customer;
        this.vendor = vendor;
        this.items = items;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestReference() {
        return requestReference;
    }

    public void setRequestReference(String requestReference) {
        this.requestReference = requestReference;
    }

    public CustomerProfile getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerProfile customer) {
        this.customer = customer;
    }

    public VendorProfile getVendor() {
        return vendor;
    }

    public void setVendor(VendorProfile vendor) {
        this.vendor = vendor;
    }

    public Set<QuotationItem> getItems() {
        return items;
    }

    public void setItems(Set<QuotationItem> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
