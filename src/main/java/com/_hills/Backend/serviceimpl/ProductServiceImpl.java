package com._hills.Backend.serviceimpl;

import com._hills.Backend.entity.Product;
import com._hills.Backend.entity.Vendor;
import com._hills.Backend.repository.ProductRepository;
import com._hills.Backend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateStock(Long productId, Integer newStock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        product.setStockQuantity(newStock);
        return productRepository.save(product);
    }

    @Override
    public Page<Product> filterProducts(Long vendorId, Long categoryId, String keyword, Pageable pageable) {
        if (vendorId != null) return productRepository.findByVendorId(vendorId, pageable);
        if (categoryId != null) return productRepository.findByCategoryId(categoryId, pageable);
        return productRepository.findAll(pageable);
    }

    @Override
    public Map<Long, List<Product>> groupProductsByVendor(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Long categoryId = product.getCategory().getId();
        List<Product> sameCategory = productRepository.findByCategoryId(categoryId,
                org.springframework.data.domain.Pageable.unpaged()).getContent();
        return sameCategory.stream()
                .collect(Collectors.groupingBy(p -> p.getVendor().getId()));
    }
}
