package com._hills.Backend.controller;

import com._hills.Backend.entity.Vendor;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/vendors")
public class VendorController {

    private final AdminService adminService;

    public VendorController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVendors(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Vendor> vendors = status != null && !status.isBlank()
                ? adminService.getVendorsByStatus(status, pageable)
                : adminService.getAllVendors(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", vendors.getContent(),
                "totalElements", vendors.getTotalElements(),
                "totalPages", vendors.getTotalPages(),
                "currentPage", vendors.getNumber(),
                "pageSize", vendors.getSize()
        ));
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        var vendors = adminService.getVendorsByStatus(AppConstants.STATUS_PENDING, pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", vendors.getContent(),
                "totalElements", vendors.getTotalElements(),
                "totalPages", vendors.getTotalPages()
        ));
    }

    @PutMapping("/{vendorId}/approve")
    public ResponseEntity<Map<String, Object>> approveVendor(
            @PathVariable Long vendorId,
            @RequestBody Map<String, String> request) {
        String remarks = request.getOrDefault("adminRemarks", "");
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor approved successfully",
                "data", adminService.approveVendor(vendorId, remarks, 1L)
        ));
    }

    @PutMapping("/{vendorId}/reject")
    public ResponseEntity<Map<String, Object>> rejectVendor(
            @PathVariable Long vendorId,
            @RequestBody Map<String, String> request) {
        String remarks = request.getOrDefault("adminRemarks", "");
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor rejected",
                "data", adminService.rejectVendor(vendorId, remarks, 1L)
        ));
    }

    @PutMapping("/{vendorId}/status")
    public ResponseEntity<Map<String, Object>> updateVendorStatus(
            @PathVariable Long vendorId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor status updated",
                "data", adminService.updateVendorStatus(vendorId, status)
        ));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getVendorCount() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "totalVendors", adminService.getTotalVendors(),
                "pendingVendors", adminService.getPendingVendorCount()
        ));
    }
}
