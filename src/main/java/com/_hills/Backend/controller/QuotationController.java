package com._hills.Backend.controller;

import com._hills.Backend.entity.Quotation;
import com._hills.Backend.entity.User;
import com._hills.Backend.entity.Vendor;
import com._hills.Backend.repository.QuotationRepository;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/quotations")
public class QuotationController {

    private final QuotationRepository quotationRepository;
    private final AdminService adminService;

    public QuotationController(QuotationRepository quotationRepository, AdminService adminService) {
        this.quotationRepository = quotationRepository;
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllQuotations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Quotation> quotations = quotationRepository.findAll(pageable);
        return ResponseEntity.ok(new HashMap<>() {{
            put("success", true);
            put("data", quotations.getContent());
            put("totalElements", quotations.getTotalElements());
            put("totalPages", quotations.getTotalPages());
        }});
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getQuotationsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Filter by status endpoint"
        ));
    }

    @PutMapping("/{quotationId}/assign-vendor")
    public ResponseEntity<Map<String, Object>> assignVendor(
            @PathVariable Long quotationId,
            @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor assigned to quotation",
                "data", Map.of("quotationId", quotationId)
        ));
    }

    @PutMapping("/{quotationId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long quotationId,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Quotation status updated"
        ));
    }

    @GetMapping("/pending-count")
    public ResponseEntity<Map<String, Object>> getPendingCount() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "pendingQuotations", 0L
        ));
    }
}
