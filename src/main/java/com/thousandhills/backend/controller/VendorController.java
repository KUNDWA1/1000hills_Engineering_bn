package com.thousandhills.backend.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thousandhills.backend.dto.ProductRequest;
import com.thousandhills.backend.dto.VendorAvailabilityRequest;
import com.thousandhills.backend.dto.VendorDocumentRequest;
import com.thousandhills.backend.dto.VendorRegistrationRequest;
import com.thousandhills.backend.model.Order;
import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.model.QuotationRequest;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.model.VendorAvailability;
import com.thousandhills.backend.model.VendorStatus;
import com.thousandhills.backend.service.OrderService;
import com.thousandhills.backend.service.ProductService;
import com.thousandhills.backend.service.QuotationService;
import com.thousandhills.backend.service.VendorService;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins = "*")
public class VendorController {

    private final VendorService vendorService;
    private final ProductService productService;
    private final OrderService orderService;
    private final QuotationService quotationService;

    public VendorController(VendorService vendorService, ProductService productService, OrderService orderService, QuotationService quotationService) {
        this.vendorService = vendorService;
        this.productService = productService;
        this.orderService = orderService;
        this.quotationService = quotationService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        return vendorService.findByUserUsername(principal.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/availability")
    public ResponseEntity<?> updateAvailability(Principal principal, @RequestBody VendorAvailabilityRequest request) {
        return vendorService.findByUserUsername(principal.getName())
                .map(vendor -> ResponseEntity.ok(vendorService.updateAvailability(vendor, request.getAvailabilityStatus())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAssignedOrders(Principal principal) {
        return vendorService.findByUserUsername(principal.getName())
                .map(vendor -> ResponseEntity.ok(orderService.findOrdersByVendor(vendor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getVendorProducts(Principal principal) {
        return vendorService.findByUserUsername(principal.getName())
                .map(vendor -> ResponseEntity.ok(productService.getVendorApprovedProducts(vendor.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/documents")
    public ResponseEntity<Object> uploadDocuments(Principal principal, @RequestBody VendorDocumentRequest request) {
        return vendorService.findByUserUsername(principal.getName())
                .map(vendor -> ResponseEntity.<Object>ok(vendorService.updateDocuments(
                        vendor,
                        request.getBusinessLicenseUrl(),
                        request.getNationalIdUrl(),
                        request.getCompanyCertificateUrl(),
                        request.getCompanyName(),
                        request.getCompanyAddress()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/quotations")
    public ResponseEntity<List<QuotationRequest>> getAssignedQuotations(Principal principal) {
        return vendorService.findByUserUsername(principal.getName())
                .map(vendor -> ResponseEntity.ok(quotationService.getVendorQuotations(vendor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(Principal principal, @PathVariable Long productId, @RequestBody ProductRequest request) {
        java.util.Optional<VendorProfile> vendorOpt = vendorService.findByUserUsername(principal.getName())
                .filter(vendor -> vendor.getStatus() == com.thousandhills.backend.model.VendorStatus.APPROVED);
        if (vendorOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        java.util.Optional<Product> productOpt = productService.getProductById(productId)
                .filter(product -> product.getVendor().getId().equals(vendorOpt.get().getId()));
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Product product = productOpt.get();
        product.setSku(request.getSku());
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setCurrency(request.getCurrency());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setStockQuantity(request.getStockQuantity());
        product.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
        product.setDescription(request.getDescription());
        product.setIsApproved(false);
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PostMapping("/products")
    public ResponseEntity<?> createVendorProduct(Principal principal, @RequestBody ProductRequest request) {
        java.util.Optional<VendorProfile> vendorOpt = vendorService.findByUserUsername(principal.getName())
                .filter(vendor -> vendor.getStatus() == com.thousandhills.backend.model.VendorStatus.APPROVED);
        if (vendorOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Vendor must be approved before posting products.");
        }
        VendorProfile vendor = vendorOpt.get();
        Product product = new Product();
        product.setSku(request.getSku());
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setCurrency(request.getCurrency());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setStockQuantity(request.getStockQuantity());
        product.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
        product.setDescription(request.getDescription());
        product.setVendor(vendor);
        product.setIsApproved(false);
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerVendor(Principal principal, @RequestBody VendorRegistrationRequest request) {
        return vendorService.findByUserUsername(principal.getName())
                .map(vendor -> {
                    VendorProfile saved = vendorService.completeVendorRegistration(
                        vendor,
                        request.getCompanyName(),
                        request.getCompanyAddress(),
                        request.getBusinessLicenseUrl(),
                        request.getNationalIdUrl(),
                        request.getCompanyCertificateUrl(),
                        request.getAvailabilityStatus()
                    );
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
