package com._hills.Backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotations")
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String quotationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_vendor_id")
    private Vendor assignedVendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_admin_id")
    private Admin assignedByAdmin;

    @Column(nullable = false, length = 500)
    private String requestDescription;

    @Column(nullable = false, length = 50)
    private String status = "PENDING";

    @Column(nullable = false, precision = 12, scale = 2)
    private java.math.BigDecimal estimatedAmount = java.math.BigDecimal.ZERO;

    @Column(length = 1000)
    private String adminRemarks;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Quotation() {}

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuotationNumber() { return quotationNumber; }
    public void setQuotationNumber(String quotationNumber) { this.quotationNumber = quotationNumber; }
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    public Vendor getAssignedVendor() { return assignedVendor; }
    public void setAssignedVendor(Vendor assignedVendor) { this.assignedVendor = assignedVendor; }
    public Admin getAssignedByAdmin() { return assignedByAdmin; }
    public void setAssignedByAdmin(Admin assignedByAdmin) { this.assignedByAdmin = assignedByAdmin; }
    public String getRequestDescription() { return requestDescription; }
    public void setRequestDescription(String requestDescription) { this.requestDescription = requestDescription; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.math.BigDecimal getEstimatedAmount() { return estimatedAmount; }
    public void setEstimatedAmount(java.math.BigDecimal estimatedAmount) { this.estimatedAmount = estimatedAmount; }
    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
