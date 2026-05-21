package com._hills.Backend.controller;

import com._hills.Backend.entity.Vendor;
import com._hills.Backend.repository.VendorRepository;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/vendors/performance")
public class VendorPerformanceController {

    private final VendorRepository vendorRepository;
    private final com._hills.Backend.service.AdminService adminService;

    public VendorPerformanceController(VendorRepository vendorRepository,
                                       com._hills.Backend.service.AdminService adminService) {
        this.vendorRepository = vendorRepository;
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getVendorPerformance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vendor> vendors = vendorRepository.findAll(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", vendors.getContent(),
                "totalElements", vendors.getTotalElements(),
                "totalPages", vendors.getTotalPages()
        ));
    }

    @GetMapping("/{vendorId}/rating")
    public ResponseEntity<Map<String, Object>> getVendorRating(@PathVariable Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return ResponseEntity.ok(Map.of(
                "success", true,
                "vendorId", vendorId,
                "companyName", vendor.getCompanyName(),
                "rating", vendor.getRating(),
                "status", vendor.getStatus(),
                "availability", vendor.getAvailability(),
                "adminNotes", vendor.getAdminNotes()
        ));
    }

    @PutMapping("/{vendorId}/availability")
    public ResponseEntity<Map<String, Object>> updateVendorAvailability(
            @PathVariable Long vendorId,
            @RequestBody Map<String, String> request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        vendor.setAvailability(request.get("availability"));
        vendorRepository.save(vendor);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor availability updated",
                "data", vendor
        ));
    }

    @PutMapping("/{vendorId}/admin-notes")
    public ResponseEntity<Map<String, Object>> updateAdminNotes(
            @PathVariable Long vendorId,
            @RequestBody Map<String, String> request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        vendor.setAdminNotes(request.get("adminNotes"));
        vendorRepository.save(vendor);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Admin notes updated",
                "data", vendor
        ));
    }
}
