package com.thousandhills.backend.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thousandhills.backend.dto.ProductRequest;
import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.model.VendorProfile;
import com.thousandhills.backend.service.ProductService;
import com.thousandhills.backend.service.VendorService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Prevents network blocking between front & back ends
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private VendorService vendorService;

    // 1. Endpoint: GET http://localhost:8080/api/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 2. Endpoint: GET http://localhost:8080/api/products/featured
    @GetMapping("/featured")
    public List<Product> getFeaturedProducts() {
        return productService.getFeaturedProducts();
    }

    // 3. Endpoint: GET http://localhost:8080/api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Endpoint: GET http://localhost:8080/api/products/{id}/related
    @GetMapping("/{id}/related")
    public List<Product> getRelatedProducts(@PathVariable Long id) {
        return productService.getRelatedProducts(id);
    }

    // 5. Vendors can submit products for approval
    @PostMapping
    public ResponseEntity<Object> createProduct(Principal principal, @RequestBody ProductRequest request) {
        return vendorService.findByUserUsername(principal.getName())
                .filter(vendor -> vendor.getStatus() == com.thousandhills.backend.model.VendorStatus.APPROVED)
                .map(vendor -> {
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
                    return ResponseEntity.<Object>ok(productService.createProduct(product));
                })
                .orElse(ResponseEntity.badRequest().body("Vendor must be approved before posting products."));
    }
}