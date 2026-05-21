package com.thousandhills.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendor_profiles")
public class VendorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "business_license_url")
    private String businessLicenseUrl;

    @Column(name = "national_id_url")
    private String nationalIdUrl;

    @Column(name = "company_certificate_url")
    private String companyCertificateUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status = VendorStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false)
    private VendorAvailability availabilityStatus = VendorAvailability.OFFLINE;

    // Vendor reliability rating (1-5 stars) for admin internal use
    @Column(name = "reliability_rating")
    private Double reliabilityRating = 0.0;

    // Number of reviews/rating count
    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private AppUser user;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();

    public VendorProfile() {
    }

    public VendorProfile(Long id, String companyName, String companyAddress, String businessLicenseUrl,
                         String nationalIdUrl, String companyCertificateUrl, VendorStatus status,
                         VendorAvailability availabilityStatus, AppUser user) {
        this.id = id;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.businessLicenseUrl = businessLicenseUrl;
        this.nationalIdUrl = nationalIdUrl;
        this.companyCertificateUrl = companyCertificateUrl;
        this.status = status;
        this.availabilityStatus = availabilityStatus;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public String getNationalIdUrl() {
        return nationalIdUrl;
    }

    public void setNationalIdUrl(String nationalIdUrl) {
        this.nationalIdUrl = nationalIdUrl;
    }

    public String getCompanyCertificateUrl() {
        return companyCertificateUrl;
    }

    public void setCompanyCertificateUrl(String companyCertificateUrl) {
        this.companyCertificateUrl = companyCertificateUrl;
    }

    public VendorStatus getStatus() {
        return status;
    }

    public void setStatus(VendorStatus status) {
        this.status = status;
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

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
