package com.thousandhills.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thousandhills.backend.model.Product;
import com.thousandhills.backend.repository.ProductRepository;

@Service
@Transactional(readOnly = true) // Optimizes connection pool usage for read-heavy operations
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 1. Get all approved items in the public catalog
    public List<Product> getAllProducts() {
        return productRepository.findByIsApprovedTrue();
    }

    // 2. Get only approved items flagged for the landing page grid
    public List<Product> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrueAndIsApprovedTrue();
    }

    // 3. Find a single approved product profile for the detail layout page
    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdAndIsApprovedTrue(id);
    }

    // 4. Fetch up to 4 related approved products within the same matching category
    public List<Product> getRelatedProducts(Long productId) {
        Optional<Product> productOpt = productRepository.findByIdAndIsApprovedTrue(productId);
        
        if (productOpt.isEmpty()) {
            return List.of(); 
        }
        
        Product product = productOpt.get();
        
        return productRepository.findByCategoryAndIdNot(product.getCategory(), productId)
                .stream()
                .limit(4)
                .collect(Collectors.toList());
    }

    public List<Product> getVendorApprovedProducts(Long vendorId) {
        return productRepository.findAllByVendorIdAndIsApprovedTrue(vendorId);
    }

    public List<Product> getPendingProducts() {
        return productRepository.findByIsApprovedFalse();
    }

    @Transactional
    public Optional<Product> approveProduct(Long productId) {
        return productRepository.findById(productId).map(product -> {
            product.setIsApproved(true);
            return productRepository.save(product);
        });
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}