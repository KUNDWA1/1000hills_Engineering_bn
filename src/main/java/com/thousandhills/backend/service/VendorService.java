package com.thousandhills.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thousandhills.backend.model.VendorAvailability;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.model.VendorStatus;
import com.thousandhills.backend.model.AppUser;
import com.thousandhills.backend.repository.VendorProfileRepository;
import com.thousandhills.backend.service.EmailNotificationService;

@Service
@Transactional
public class VendorService {

    private final VendorProfileRepository vendorProfileRepository;
    private final AppUserService appUserService;
    private final EmailNotificationService emailNotificationService;

    public VendorService(VendorProfileRepository vendorProfileRepository, AppUserService appUserService, EmailNotificationService emailNotificationService) {
        this.vendorProfileRepository = vendorProfileRepository;
        this.appUserService = appUserService;
        this.emailNotificationService = emailNotificationService;
    }

    public Optional<VendorProfile> findByUserUsername(String username) {
        return appUserService.findByUsername(username)
                .flatMap(user -> vendorProfileRepository.findByUserId(user.getId()));
    }

    public Optional<VendorProfile> findById(Long vendorId) {
        return vendorProfileRepository.findById(vendorId);
    }

    public List<VendorProfile> getPendingVendors() {
        return vendorProfileRepository.findByStatus(VendorStatus.PENDING);
    }

    public VendorProfile updateAvailability(VendorProfile vendorProfile, VendorAvailability availability) {
        vendorProfile.setAvailabilityStatus(availability);
        return vendorProfileRepository.save(vendorProfile);
    }

    public VendorProfile updateDocuments(VendorProfile vendorProfile, String businessLicenseUrl, String nationalIdUrl, String companyCertificateUrl, String companyName, String companyAddress) {
        if (businessLicenseUrl != null) {
            vendorProfile.setBusinessLicenseUrl(businessLicenseUrl);
        }
        if (nationalIdUrl != null) {
            vendorProfile.setNationalIdUrl(nationalIdUrl);
        }
        if (companyCertificateUrl != null) {
            vendorProfile.setCompanyCertificateUrl(companyCertificateUrl);
        }
        if (companyName != null) {
            vendorProfile.setCompanyName(companyName);
        }
        if (companyAddress != null) {
            vendorProfile.setCompanyAddress(companyAddress);
        }
        vendorProfile.setStatus(VendorStatus.PENDING);
        return vendorProfileRepository.save(vendorProfile);
    }

    public VendorProfile completeVendorRegistration(VendorProfile vendorProfile, String companyName, String companyAddress, String businessLicenseUrl, String nationalIdUrl, String companyCertificateUrl, String availabilityStatus) {
        vendorProfile.setCompanyName(companyName);
        vendorProfile.setCompanyAddress(companyAddress);
        vendorProfile.setBusinessLicenseUrl(businessLicenseUrl);
        vendorProfile.setNationalIdUrl(nationalIdUrl);
        vendorProfile.setCompanyCertificateUrl(companyCertificateUrl);
        
        // Convert string availability status to enum
        VendorAvailability availability = VendorAvailability.OFFLINE; // default
        try {
            availability = VendorAvailability.valueOf(availabilityStatus);
        } catch (IllegalArgumentException e) {
            // Keep default OFFLINE if invalid
        }
        vendorProfile.setAvailabilityStatus(availability);
        vendorProfile.setStatus(VendorStatus.PENDING);
        
        return vendorProfileRepository.save(vendorProfile);
    }

    public VendorProfile approveVendor(Long vendorId) {
        VendorProfile profile = vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorId));
        profile.setStatus(VendorStatus.APPROVED);
        profile.setAvailabilityStatus(VendorAvailability.AVAILABLE);
        VendorProfile saved = vendorProfileRepository.save(profile);
        emailNotificationService.notifyVendorApproved(saved);
        return saved;
    }

    public VendorProfile rejectVendor(Long vendorId) {
        VendorProfile profile = vendorProfileRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found: " + vendorId));
        profile.setStatus(VendorStatus.REJECTED);
        profile.setAvailabilityStatus(VendorAvailability.OFFLINE);
        VendorProfile saved = vendorProfileRepository.save(profile);
        emailNotificationService.notifyVendorRejected(saved);
        return saved;
    }
}
