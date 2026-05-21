package com.thousandhills.backend.dto;

import com.thousandhills.backend.model.VendorAvailability;

public class VendorMatchDto {
    private Long vendorId;
    private String companyName;
    private VendorAvailability availabilityStatus;
    private Double reliabilityRating;
    private Integer stockQuantity;

    // Getters and Setters
    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public VendorAvailability getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(VendorAvailability availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Double getReliabilityRating() {
        return reliabilityRating;
    }

    public void setReliabilityRating(Double reliabilityRating) {
        this.reliabilityRating = reliabilityRating;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}