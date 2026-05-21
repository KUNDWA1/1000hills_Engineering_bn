package com._hills.Backend.controller;

import com._hills.Backend.entity.Quotation;
import com._hills.Backend.repository.QuotationRepository;
import com._hills.Backend.service.AdminService;
import com._hills.Backend.service.VendorManager;
import com._hills.Backend.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/quotations")
public class QuotationController {

    private final QuotationRepository quotationRepository;
    private final AdminService adminService;
    private final VendorManager vendorManager;

    public QuotationController(QuotationRepository quotationRepository, AdminService adminService, VendorManager vendorManager) {
        this.quotationRepository = quotationRepository;
        this.adminService = adminService;
        this.vendorManager = vendorManager;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllQuotations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Quotation> quotations = quotationRepository.findAll(pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", quotations.getContent(),
                "totalElements", quotations.getTotalElements(),
                "totalPages", quotations.getTotalPages(),
                "currentPage", quotations.getNumber(),
                "pageSize", quotations.getSize()
        ));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getQuotationsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Quotation> quotations = quotationRepository.findByAssignedVendorId(
                Long.parseLong(status.equalsIgnoreCase("ALL") ? "0" : "0"), pageable);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Quotations by status endpoint — filter " + status,
                "data", quotations.getContent()
        ));
    }

    @PutMapping("/{quotationId}/assign-vendor")
    public ResponseEntity<Map<String, Object>> assignVendor(
            @PathVariable Long quotationId,
            @RequestBody Map<String, Object> request) {
        Long vendorId = ((Number) request.get("vendorId")).longValue();
        Long adminId = request.get("adminId") != null ? ((Number) request.get("adminId")).longValue() : 1L;
        String remarks = (String) request.getOrDefault("adminRemarks", "");
        Quotation quotation = vendorManager.assignVendorToQuotation(quotationId, vendorId, adminId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Vendor assigned to quotation",
                "data", quotation
        ));
    }

    @PutMapping("/{quotationId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long quotationId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        String remarks = request.getOrDefault("adminRemarks", "");
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + quotationId));
        quotation.setStatus(status);
        if (remarks != null && !remarks.isBlank()) {
            quotation.setAdminRemarks(remarks);
        }
        quotationRepository.save(quotation);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Quotation status updated to " + status,
                "data", quotation
        ));
    }

    @GetMapping("/pending-count")
    public ResponseEntity<Map<String, Object>> getPendingCount() {
        long pending = quotationRepository.countByStatus(AppConstants.STATUS_PENDING);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "pendingQuotations", pending
        ));
    }
}
