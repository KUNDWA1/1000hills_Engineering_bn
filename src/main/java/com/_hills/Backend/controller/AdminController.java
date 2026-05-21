package com._hills.Backend.controller;

import com._hills.Backend.dto.AdminDashboardResponse;
import com._hills.Backend.dto.AdminResponse;
import com._hills.Backend.entity.Admin;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        AdminDashboardResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", stats
        ));
    }

    @GetMapping("/sales-report")
    public ResponseEntity<Map<String, Object>> getSalesReport() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", adminService.getSalesReport()
        ));
    }

    @GetMapping("/profile/{adminId}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Long adminId) {
        Admin admin = adminService.getAdminProfile(adminId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", AdminResponse.from(admin)
        ));
    }

    @GetMapping("/total-vendors")
    public ResponseEntity<Map<String, Object>> getTotalVendors() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of("totalVendors", adminService.getTotalVendors())
        ));
    }

    @GetMapping("/pending-vendors")
    public ResponseEntity<Map<String, Object>> getPendingVendors() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of("pendingCount", adminService.getPendingVendorCount())
        ));
    }

    @GetMapping("/total-customers")
    public ResponseEntity<Map<String, Object>> getTotalCustomers() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of("totalCustomers", adminService.getTotalCustomers())
        ));
    }
}
