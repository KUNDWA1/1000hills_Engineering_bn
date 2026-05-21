package com._hills.Backend.controller;

import com._hills.Backend.entity.Product;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductController {

    private final AdminService adminService;

    public ProductController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = adminService.getAllProducts(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", products.getContent(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages()
        ));
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = adminService.getPendingProducts(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", products.getContent(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages()
        ));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = adminService.getLowStockProducts(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", products.getContent(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages()
        ));
    }

    @PutMapping("/{productId}/approve")
    public ResponseEntity<Map<String, Object>> approveProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product approved",
                "data", adminService.approveProduct(productId, 1L)
        ));
    }

    @PutMapping("/{productId}/reject")
    public ResponseEntity<Map<String, Object>> rejectProduct(
            @PathVariable Long productId,
            @RequestBody Map<String, String> request) {
        String remarks = request.getOrDefault("adminRemarks", "Rejected by admin");
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product rejected",
                "data", adminService.rejectProduct(productId, remarks, 1L)
        ));
    }

    @PutMapping("/{productId}/availability")
    public ResponseEntity<Map<String, Object>> updateAvailability(
            @PathVariable Long productId,
            @RequestBody Map<String, Boolean> request) {
        boolean available = request.getOrDefault("available", true);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product availability updated",
                "data", adminService.updateProductAvailability(productId, available)
        ));
    }
}
