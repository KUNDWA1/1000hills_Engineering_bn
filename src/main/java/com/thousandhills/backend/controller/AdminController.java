package com.thousandhills.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thousandhills.backend.dto.AssignVendorRequest;
import com.thousandhills.backend.model.Order;
import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.model.QuotationRequest;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.service.OrderService;
import com.thousandhills.backend.service.ProductService;
import com.thousandhills.backend.service.QuotationService;
import com.thousandhills.backend.service.VendorService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final VendorService vendorService;
    private final OrderService orderService;
    private final QuotationService quotationService;
    private final ProductService productService;

    public AdminController(VendorService vendorService, OrderService orderService, QuotationService quotationService, ProductService productService) {
        this.vendorService = vendorService;
        this.orderService = orderService;
        this.quotationService = quotationService;
        this.productService = productService;
    }

    @GetMapping("/vendors/pending")
    public ResponseEntity<List<VendorProfile>> getPendingVendors() {
        return ResponseEntity.ok(vendorService.getPendingVendors());
    }

    @PutMapping("/vendors/{id}/approve")
    public ResponseEntity<VendorProfile> approveVendor(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.approveVendor(id));
    }

    @PutMapping("/vendors/{id}/reject")
    public ResponseEntity<VendorProfile> rejectVendor(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.rejectVendor(id));
    }

    @PostMapping("/orders/{orderId}/assign")
    public ResponseEntity<Object> assignVendorToOrder(@PathVariable Long orderId, @RequestBody AssignVendorRequest request) {
        return vendorService.findById(request.getVendorId())
                .flatMap(vendor -> orderService.assignVendor(orderId, vendor))
                .map(order -> ResponseEntity.<Object>ok(order))
                .orElse(ResponseEntity.badRequest().body("Order or vendor not found."));
    }

    @GetMapping("/products/pending")
    public ResponseEntity<List<Product>> getPendingProducts() {
        return ResponseEntity.ok(productService.getPendingProducts());
    }

    @PutMapping("/products/{id}/approve")
    public ResponseEntity<Object> approveProduct(@PathVariable Long id) {
        return productService.approveProduct(id)
                .map(product -> ResponseEntity.<Object>ok(product))
                .orElse(ResponseEntity.badRequest().body("Product not found."));
    }

    @GetMapping("/quotations/pending")
    public ResponseEntity<List<QuotationRequest>> getPendingQuotations() {
        return ResponseEntity.ok(quotationService.getPendingQuotations());
    }

    @PostMapping("/quotations/{quotationId}/assign")
    public ResponseEntity<Object> assignVendorToQuotation(@PathVariable Long quotationId, @RequestBody AssignVendorRequest request) {
        return vendorService.findById(request.getVendorId())
                .flatMap(vendor -> quotationService.assignVendor(quotationId, vendor))
                .map(quotation -> ResponseEntity.<Object>ok(quotation))
                .orElse(ResponseEntity.badRequest().body("Quotation or vendor not found."));
    }
}
